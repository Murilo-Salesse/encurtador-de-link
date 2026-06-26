package com.salessew.adapter.out.persistence;

import com.salessew.core.domain.Link;
import com.salessew.core.port.out.AnalyticsRepositoryPortOut;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

import static com.salessew.adapter.out.persistence.DynamoDbAttributeConstants.*;
import static java.lang.String.format;


@Component
public class AnalyticsDynamoDbAdapterOut implements AnalyticsRepositoryPortOut {

    private final DynamoDbTemplate dynamoDbTemplate;
    private final DynamoDbClient dynamoDbClient;

    public AnalyticsDynamoDbAdapterOut(DynamoDbTemplate dynamoDbTemplate, DynamoDbClient dynamoDbClient) {
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public void updateClickCount(Link link) {

        var date = LocalDate.now();

        var key = Key.builder()
                .partitionValue(link.getLinkId())
                .sortValue(date.toString())
                .build();

        var entity = dynamoDbTemplate.load(key, LinkAnalyticsEntity.class);

        if (entity != null) {
            updateAnalytics(entity, date);
        } else {
            dynamoDbTemplate.save(LinkAnalyticsEntity.fromDomain(link, date));
        }
    }

    private void updateAnalytics(LinkAnalyticsEntity entity, LocalDate date) {

        Map<String, AttributeValue> key = Map.of(
                ANALYTICS_LINK_ID, AttributeValue.fromS(entity.getLinkId()),
                ANALYTICS_DATE, AttributeValue.fromS(date.toString())
        );

        Map<String, AttributeValue> expressionValues = Map.of(
                ":zero", AttributeValue.fromN("0"),
                ":inc", AttributeValue.fromN("1"),
                ":now", AttributeValue.fromS(Instant.now().toString())
        );

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName("tb_links_analytics")
                .key(key)
                .updateExpression(format("SET %s = if_not_exists(%s, :zero) + :inc, updated_at = :now",
                        ANALYTICS_CLICKS,
                        ANALYTICS_CLICKS))
                .expressionAttributeValues(expressionValues)
                .build();

        dynamoDbClient.updateItem(request);
    }
}
