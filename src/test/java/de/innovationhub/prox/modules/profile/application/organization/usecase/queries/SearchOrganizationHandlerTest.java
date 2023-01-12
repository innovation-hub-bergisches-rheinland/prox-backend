package de.innovationhub.prox.modules.profile.application.organization.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.TagView;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class SearchOrganizationHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  TagFacade tagFacade = mock(TagFacade.class);
  SearchOrganizationHandler handler = new SearchOrganizationHandler(organizationRepository, tagFacade);

  @Test
  void shouldCallRepository() {
    var query = "lol";
    var page = PageRequest.of(0, 10);

    handler.handle(query, null, page);

    verify(organizationRepository).search(eq(query), anyCollection(), eq(page));
  }

  @Test
  void shouldCallRepositoryWithResolvedTags() {
    var givenTags = List.of(
        new TagView(UUID.randomUUID(), "tag1")
    );
    when(tagFacade.getTagsByName(anyCollection())).thenReturn(givenTags);

    handler.handle(null, List.of("tag1"), Pageable.unpaged());

    ArgumentCaptor<List<UUID>> captor = ArgumentCaptor.forClass((Class) List.class);
    verify(organizationRepository).search(any(), captor.capture(), any());
    assertThat(captor.getValue()).containsExactly(givenTags.get(0).id());
  }
}