package com.salessew.adapter.out.persistence;

import com.salessew.adapter.out.persistence.helper.LinkDynamoDbTokenHelper;
import com.salessew.core.domain.Link;
import com.salessew.core.domain.PaginatedResult;
import com.salessew.core.port.out.LinkRepositoryPortOut;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.salessew.config.Constants.FK_TB_USERS_LINK_USER_INDEX;

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
    public PaginatedResult<Link> findAllByUserId(String userId, String nextToken, int limit) {

        QueryConditional qc = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(userId)
                        .build()
        );

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(qc)
                .limit(limit);

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
