package de.innovationhub.prox.modules.user.domain.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import de.innovationhub.prox.modules.user.domain.search.events.SearchPreferencesCreated;
import de.innovationhub.prox.modules.user.domain.search.events.SearchPreferencesTagCollectionUpdated;
import de.innovationhub.prox.modules.user.domain.search.events.SearchPreferencesUpdated;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SearchPreferencesTest {

  private SearchPreferences searchPreferencesUnderTest = SearchPreferences.create(
      UUID.randomUUID(),
      new ProjectSearch(),
      new OrganizationSearch(),
      new LecturerSearch()
  );

  @Test
  void testCreate() {
    var userId = UUID.randomUUID();
    var projectSearch = new ProjectSearch();
    var organizationSearch = new OrganizationSearch();
    var lecturerSearch = new LecturerSearch();

    final SearchPreferences result = SearchPreferences.create(
        userId,
        projectSearch,
        organizationSearch,
        lecturerSearch
    );

    assertEquals(userId, result.getUserId());
    assertEquals(projectSearch, result.getProjectSearch());
    assertEquals(organizationSearch, result.getOrganizationSearch());
    assertEquals(lecturerSearch, result.getLecturerSearch());

    assertThat(result.getDomainEvents())
        .filteredOn(event -> event instanceof SearchPreferencesCreated)
        .hasSize(1);
  }

  @Test
  void testUpdate() {
    var projectSearch = new ProjectSearch();
    var organizationSearch = new OrganizationSearch();
    var lecturerSearch = new LecturerSearch();

    searchPreferencesUnderTest.update(
        projectSearch,
        organizationSearch,
        lecturerSearch
    );

    assertEquals(projectSearch, searchPreferencesUnderTest.getProjectSearch());
    assertEquals(organizationSearch, searchPreferencesUnderTest.getOrganizationSearch());
    assertEquals(lecturerSearch, searchPreferencesUnderTest.getLecturerSearch());

    assertThat(searchPreferencesUnderTest.getDomainEvents())
        .filteredOn(event -> event instanceof SearchPreferencesUpdated)
        .hasSize(1);
  }

  @Test
  void testSetTagCollectionUpdate() {
    var tagCollectionId = UUID.randomUUID();

    searchPreferencesUnderTest.setTagCollection(tagCollectionId);

    assertEquals(tagCollectionId, searchPreferencesUnderTest.getTagCollectionId());

    assertThat(searchPreferencesUnderTest.getDomainEvents())
        .filteredOn(event -> event instanceof SearchPreferencesTagCollectionUpdated)
        .hasSize(1);
  }
}