package de.innovationhub.prox.modules.user.application.search;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferences;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferencesRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

class UserProfileTaggedEventListenerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  UserProfileRepository userProfileRepository;

  @Autowired
  SearchPreferencesRepository searchPreferencesRepository;

  @Test
  @Transactional
  void shouldUpdateTagCollectionInSearchPreferences() {
    var up = Instancio.of(UserProfile.class).create();
    userProfileRepository.save(up);
    var sp = Instancio.of(SearchPreferences.class)
            .supply(field(SearchPreferences::getUserId), up::getUserId)
            .create();
    searchPreferencesRepository.save(sp);

    var tagCollectionId = UUID.randomUUID();
    up.setTagCollectionId(tagCollectionId);
    userProfileRepository.save(up);

    var foundSp = searchPreferencesRepository.findByUserId(sp.getUserId());
    assertEquals(tagCollectionId, foundSp.get().getTagCollectionId());
  }
}