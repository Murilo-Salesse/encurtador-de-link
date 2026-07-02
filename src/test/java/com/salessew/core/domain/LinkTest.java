package com.salessew.core.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {

    @Nested
    class execute {

        @Test
        void shouldGenerateFullUrlWhenThereIsNoUtmTags() {
            // Arrange - Preparar todos os mocks, stubs, variáveis.
            String originalUrl = "https://example.com";
            UtmTags utmTags = new UtmTags(
                    null,
                    null,
                    null,
                    null
            );

            Link link = new Link(
                    UUID.randomUUID().toString(),
                    originalUrl,
                    utmTags,
                    null,
                    false,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            // Act & Assert- Execute o método a ser testado.
            var fullUrl = link.generateFullUrl();

            // Assert - Verificar se o teste teve o comportamento esperado.
            assertEquals(originalUrl, fullUrl);
        }

        @Test
        void shouldGenerateFullUrlWhenThereIsUtmTags() {
            // Arrange - Preparar todos os mocks, stubs, variáveis.
            String originalUrl = "https://example.com";
            UtmTags utmTags = new UtmTags(
                    "ig",
                    "paid_social",
                    "release",
                    "post_live"
            );

            Link link = new Link(
                    UUID.randomUUID().toString(),
                    originalUrl,
                    utmTags,
                    null,
                    false,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            // Act & Assert- Execute o método a ser testado.
            var fullUrl = link.generateFullUrl();

            // Assert - Verificar se o teste teve o comportamento esperado.
            var expectedFullUrl = "https://example.com?utm_source=ig&utm_medium=paid_social&utm_campaign=release&utm_content=post_live";
            assertEquals(expectedFullUrl, fullUrl);
        }
    }
}