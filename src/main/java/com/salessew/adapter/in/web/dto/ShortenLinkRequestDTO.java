package com.salessew.adapter.in.web.dto;

import com.salessew.core.domain.Link;
import com.salessew.core.domain.User;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShortenLinkRequestDTO(@NotBlank String uniqueLinkSlug,
                                    @NotBlank String originalUrl,
                                    UtmTagsRequest utm,
                                    LocalDateTime expirationDateTime) {

    public Link toDomain(UUID userId) {
        return new Link(
                uniqueLinkSlug,
                originalUrl,
                utm != null ? utm.toDomain() : null,
                new User(userId),
                true,
                expirationDateTime,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
