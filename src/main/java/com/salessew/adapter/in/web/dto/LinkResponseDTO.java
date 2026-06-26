package com.salessew.adapter.in.web.dto;

import com.salessew.core.domain.Link;

import java.time.LocalDateTime;

public record LinkResponseDTO(String linkId,
                              String originalUrl,
                              boolean active,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {

    public static LinkResponseDTO fromDomain(Link link) {
        return new LinkResponseDTO(
                link.getLinkId(),
                link.getOriginalUrl(),
                link.isActive(),
                link.getCreatedAt(),
                link.getUpdatedAt()
        );
    }
}
