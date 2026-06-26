package com.salessew.adapter.in.web.dto;

import com.salessew.core.domain.User;
import java.time.LocalDateTime;

public record CreateUserResponseDTO(String userId,
                                    LocalDateTime createdAt) {

    public static CreateUserResponseDTO fromDomain(User user) {
        return new CreateUserResponseDTO(user.getUserId().toString(), user.getCreatedAt());
    }
}
