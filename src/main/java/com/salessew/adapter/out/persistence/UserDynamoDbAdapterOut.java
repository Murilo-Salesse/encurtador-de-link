package com.salessew.adapter.out.persistence;

import com.salessew.core.domain.User;
import com.salessew.core.port.out.UserRepositoryPortOut;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Optional;
import java.util.UUID;

import static com.salessew.config.Constants.EMAIL_INDEX;

@Component
public class UserDynamoDbAdapterOut implements UserRepositoryPortOut {

    private final Logger logger = LoggerFactory.getLogger(UserDynamoDbAdapterOut.class);
    private final DynamoDbTemplate dynamoDbTemplate;

    public UserDynamoDbAdapterOut(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public User save(User user) {

        logger.debug("Executing save user into DynamoDB: {}", user);
        var entity = UserEntity.fromDomain(user);
        dynamoDbTemplate.save(entity);

        logger.debug("Ending save user into DynamoDB: {}", user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {

        logger.debug("Executing FindUserByEmail on DynamoDB: {}", email);
        var cond = QueryConditional.keyEqualTo(k ->
                k.partitionValue(AttributeValue.builder().s(email).build()));

        var query = QueryEnhancedRequest.builder()
                .queryConditional(cond)
                .build();

        var result = dynamoDbTemplate.query(query, UserEntity.class, EMAIL_INDEX);

        var opt = result.stream()
                .flatMap(userEntityPage -> userEntityPage.items().stream())
                .map(UserEntity::toDomain)
                .findFirst();

        logger.debug("Ending executed on DynamoDB: {}", opt);
        return opt;
    }

    @Override
    public void deleteById(UUID userId) {

        logger.debug("Executing delete user by ID on DynamoDB: {}", userId);
        var key = Key.builder()
                .partitionValue(userId.toString())
                .build();

        dynamoDbTemplate.delete(key, UserEntity.class);
        logger.debug("Ending delete user by ID on DynamoDB: {}", userId);
    }

    @Override
    public Optional<User> findById(UUID userId) {

        logger.debug("Executing find user by ID on DynamoDB: {}", userId);
        var key = Key.builder()
                .partitionValue(userId.toString())
                .build();

        var entity = dynamoDbTemplate.load(key, UserEntity.class);

        Optional<User> opt = entity == null ?
                Optional.empty()
                : Optional.of(entity.toDomain());

        logger.debug("Ending find user by ID on DynamoDB: {}", opt);

        return opt;
    }
}
