package de.innovationhub.prox.modules.user.application.profile.usecase.queries;

import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindAllUserProfilesByUserIdsHandlerTest {
  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);

  FindAllUserProfilesByUserIdsHandler findAllUserProfilesByUserIdsHandler = new FindAllUserProfilesByUserIdsHandler(userProfileRepository);

  @Test
  void shouldFindTwo(){
    var up1 = createDummyUserProfile();
    var up2 = createDummyUserProfile();
    var idList = new ArrayList<>(Arrays.asList(up1.getUserId(),up2.getUserId()));
    when(userProfileRepository.findByUserId(up1.getUserId())).thenReturn(Optional.of(up1));
    when(userProfileRepository.findByUserId(up2.getUserId())).thenReturn(Optional.of(up2));
    var result = findAllUserProfilesByUserIdsHandler.handle(idList);
    assert result.containsAll(Arrays.asList(up1,up2));
  }

  private UserProfile createDummyUserProfile() {
    return UserProfile.create(UUID.randomUUID(), "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), true);
  }

}
