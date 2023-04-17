package de.innovationhub.prox.modules.organization.application.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class SearchOrganizationHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  TagFacade tagFacade = mock(TagFacade.class);
  TagCollectionFacade tagCollectionFacade = mock(TagCollectionFacade.class);
  SearchOrganizationHandler handler = new SearchOrganizationHandler(organizationRepository, tagFacade, tagCollectionFacade);

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
        new TagDto(UUID.randomUUID(), "tag1", Instant.now(), Instant.now())
    );
    var givenTagCollections = List.of(
        new TagCollectionDto(UUID.randomUUID(), givenTags)
    );
    var givenTagsIds = givenTagCollections.stream().map(TagCollectionDto::id).toList();
    when(tagFacade.getTagsByName(anyCollection())).thenReturn(givenTags);
    when(tagCollectionFacade.findWithAllTags(anyCollection())).thenReturn(givenTagCollections);

    handler.handle(null, List.of("tag1"), Pageable.unpaged());
    verify(organizationRepository).search(any(), assertArg(ids -> assertThat(givenTagsIds).containsExactlyElementsOf(ids)), any());
  }
}