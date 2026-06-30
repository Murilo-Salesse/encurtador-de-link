package com.salessew.adapter.out.persistence;

import com.salessew.adapter.out.persistence.helper.LinkDynamoDbTokenHelper;
import com.salessew.core.domain.Link;
import com.salessew.core.domain.LinkFilter;
import com.salessew.core.domain.PaginatedResult;
import com.salessew.core.port.out.LinkRepositoryPortOut;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.salessew.config.Constants.FK_TB_USERS_LINK_USER_INDEX;
import static java.util.Objects.isNull;

@Component
public class LinkDynamoDbAdapterOut implements LinkRepositoryPortOut {

    private final DynamoDbTemplate dynamoDbTemplate;
    private final LinkDynamoDbTokenHelper linkDynamoDbTokenHelper;

    public LinkDynamoDbAdapterOut(DynamoDbTemplate dynamoDbTemplate, LinkDynamoDbTokenHelper linkDynamoDbTokenHelper) {
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.linkDynamoDbTokenHelper = linkDynamoDbTokenHelper;
    }

    @Override
    public Link save(Link link) {

        var entity = LinkEntity.fromDomain(link);
        dynamoDbTemplate.save(entity);

        return link;
    }

    @Override
    public Optional<Link> findById(String linkId) {

        var key = Key.builder()
                .partitionValue(linkId)
                .build();

        var entity = dynamoDbTemplate.load(key, LinkEntity.class);

        return entity == null ?
                Optional.empty()
                : Optional.of(entity.toDomain());
    }

    @Override
    public PaginatedResult<Link> findAllByUserId(String userId,
                                                 String nextToken,
                                                 int limit,
                                                 LinkFilter filters) {

        QueryConditional qc = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(userId)
                        .build()
        );

        List<String> conditions = new ArrayList<>();
        Map<String, AttributeValue> expValues = new HashMap<>();

        if (!isNull(filters.active())) {
            conditions.add("active = :activeValue");
            expValues.put(":activeValue", AttributeValue.fromBool(filters.active()));
        }

        if (!isNull(filters.startCreatedAt()) && !isNull(filters.endCreatedAt())) {
            conditions.add("created_at BETWEEN :startCreatedAt AND :endCreatedAt");
            expValues.put(":startCreatedAt", AttributeValue.fromS(LocalDateTime.of(filters.startCreatedAt(), LocalTime.MIN).toString()));
            expValues.put(":endCreatedAt", AttributeValue.fromS(LocalDateTime.of(filters.endCreatedAt(), LocalTime.MAX).toString()));
        }

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(qc)
                .limit(limit);

        // Filtros
        if (!conditions.isEmpty()) {
            requestBuilder.filterExpression(Expression.builder()
                    .expression(String.join(" AND ", conditions))
                    .expressionValues(expValues)
                    .build());
        }

        if (nextToken != null && !nextToken.isEmpty()) {
            var map = linkDynamoDbTokenHelper.decodeStartToken(nextToken);
            requestBuilder.exclusiveStartKey(map);
        }

        Page<LinkEntity> page = dynamoDbTemplate
                .query(requestBuilder.build(), LinkEntity.class, FK_TB_USERS_LINK_USER_INDEX)
                .stream()
                .findFirst()
                .orElse(null);

                if (page == null) {
                    return new PaginatedResult<>(Collections.emptyList(), null, false);
                }

                var links = page
                        .items()
                        .stream()
                        .map(LinkEntity::toDomain)
                        .collect(Collectors.toList());

                return new PaginatedResult<>(
                        links,
                        page.lastEvaluatedKey() != null ? linkDynamoDbTokenHelper.encodeStartToken(page.lastEvaluatedKey()) : "",
                        page.lastEvaluatedKey() != null
                );
    }
}
