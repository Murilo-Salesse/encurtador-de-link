package com.salessew.adapter.out.persistence;

import com.salessew.core.domain.Link;
import com.salessew.core.port.out.LinkRepositoryPortOut;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@Component
public class LinkDynamoDbAdapterOut implements LinkRepositoryPortOut {

    private final DynamoDbTemplate dynamoDbTemplate;

    public LinkDynamoDbAdapterOut(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
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
}
