package de.innovationhub.prox.modules.user.application.user.usecase.queries;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.user.domain.user.ProxUserAccountRepository;
import org.junit.jupiter.api.Test;

class SearchUserHandlerTest {
  ProxUserAccountRepository proxUserAccountRepository = mock(ProxUserAccountRepository.class);
  SearchUserHandler searchUserHandler = new SearchUserHandler(proxUserAccountRepository);

  @Test
  void shouldThrowOnNullQuery() {
    assertThrows(NullPointerException.class, () -> searchUserHandler.handle(null, null));
  }

  @Test
  void shouldThrowOnShortQuery() {
    assertThrows(IllegalArgumentException.class, () -> searchUserHandler.handle("a", null));
  }

  @Test
  void shouldSearchWithoutRole() {
    searchUserHandler.handle("abcdefg", null);
    verify(proxUserAccountRepository).search("abcdefg");
  }

  @Test
  void shouldSearchWithRole() {
    searchUserHandler.handle("abcdefg", "professor");
    verify(proxUserAccountRepository).searchWithRole("abcdefg", "professor");
  }
}