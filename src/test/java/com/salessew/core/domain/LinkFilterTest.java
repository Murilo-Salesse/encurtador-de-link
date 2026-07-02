package com.salessew.core.domain;

import com.salessew.core.exception.FilterException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LinkFilterTest {

    @Nested
    class validate {

        @Test
        void shouldNotThrowFilterExceptionWhenStartCreatedAtIsNull() {
            // Arrange - Preparar todos os mocks, stubs, variáveis.
            LocalDate endCreatedAt = LocalDate.now().plusDays(3);

            // Act & Assert- Execute o método a ser testado.
            var linkFilter = new LinkFilter(null, null, endCreatedAt);
            assertDoesNotThrow(linkFilter::validate);

            // Assert - Verificar se o teste teve o comportamento esperado.
        }

        @Test
        void shouldNotThrowFilterExceptionWhenEndCreatedAtIsNull() {
            // Arrange
            LocalDate startCreatedAt = LocalDate.now();

            // Act & Assert
            var linkFilter = new LinkFilter(null, startCreatedAt, null);
            assertDoesNotThrow(linkFilter::validate);
        }

        @Test
        void shouldNotThrowFilterExceptionWhenBothCreatedAtIsNull() {
            var linkFilter = new LinkFilter(null, null, null);
            assertDoesNotThrow(linkFilter::validate);
        }

        @Test
        void shouldNotThrowFilterExceptionWhenStartCreatedAtIsAfterEndCreatedAt() {
            // Arrange - Preparar todos os mocks, stubs, variáveis.
            LocalDate endCreatedAt = LocalDate.now().plusDays(3);
            LocalDate startCreatedAt = endCreatedAt.plusDays(7);

            // Act & Assert- Execute o método a ser testado.
            var linkFilter = new LinkFilter(null, startCreatedAt, endCreatedAt);
            assertThrows(FilterException.class, linkFilter::validate);

            // Assert - Verificar se o teste teve o comportamento esperado.
        }
    }

}