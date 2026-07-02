package com.salessew.core.usecase;

import com.salessew.core.domain.Link;
import com.salessew.core.domain.LinkFilter;
import com.salessew.core.domain.PaginatedResult;
import com.salessew.core.exception.FilterException;
import com.salessew.core.port.out.LinkRepositoryPortOut;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLinksUseCaseTest {

    @Mock
    LinkFilter linkFilter;

    @Mock
    LinkRepositoryPortOut linkRepositoryPortOut;

    @InjectMocks
    UserLinksUseCase userLinksUseCase;

    @Nested
    class execute {

        @Test
        void shouldCallFindAllByUserId() {

            // Arrange - Preparar todos os mocks, stubs, variáveis.
            PaginatedResult<Link> userLinks = new PaginatedResult<>(new ArrayList<>(), null, false);
            String userId = UUID.randomUUID().toString();
            String nextToken = "dasdasd";
            int limit = 10;

            doNothing().when(linkFilter).validate();
            doReturn(userLinks).when(linkRepositoryPortOut)
                    .findAllByUserId(userId, nextToken, limit, linkFilter);

            // Act - Execute o método a ser testado.
            var output = userLinksUseCase.execute(userId, nextToken, limit, linkFilter);

            // Assert - Verificar se o teste teve o comportamento esperado.
            verify(linkRepositoryPortOut, times(1))
                    .findAllByUserId(userId, nextToken, limit, linkFilter);
            assertEquals(userLinks, output);
        }

        @Test
        void shouldNotCallFindAllByUserIdWhenFilterException() {

            // Arrange - Preparar todos os mocks, stubs, variáveis.
            String userId = UUID.randomUUID().toString();
            String nextToken = "dasdasd";
            int limit = 10;

            doThrow(FilterException.class).when(linkFilter).validate();

            // Act & Assert - Execute o método a ser testado.
            assertThrows(FilterException.class,
                    () -> userLinksUseCase.execute(userId, nextToken, limit, linkFilter));

            verify(linkRepositoryPortOut, times(0))
                    .findAllByUserId(anyString(), anyString(), anyInt(), any(LinkFilter.class));
        }
    }

}