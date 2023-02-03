package de.innovationhub.prox.modules.user.application.profile.usecase.queries;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import org.junit.jupiter.api.Test;

class SearchUserHandlerTest {

  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  SearchUserHandler searchUserHandler = new SearchUserHandler(userProfileRepository);

  @Test
  void shouldSearch() {
    searchUserHandler.handle("abcdefg", null);
    verify(userProfileRepository).search("abcdefg", null);
  }
}