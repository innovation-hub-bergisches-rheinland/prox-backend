package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.user.contract.profile.dto.LecturerProfileDto;
import de.innovationhub.prox.modules.user.application.profile.dto.LecturerProfileInformationDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto.ContactInformationDto;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class SearchProjectHandlerTest {

  ProjectRepository projectRepository = mock(ProjectRepository.class);
  TagFacade tagFacade = mock(TagFacade.class);
  UserProfileFacade userProfileFacade = mock(UserProfileFacade.class);
  TagCollectionFacade tagCollectionFacade = mock(TagCollectionFacade.class);
  SearchProjectHandler searchProjectHandler = new SearchProjectHandler(projectRepository, tagFacade,
      userProfileFacade, tagCollectionFacade);

  @Test
  void shouldCallRepository() {
    var status = List.of(ProjectState.STALE);
    var keys = List.of("key");
    var modules = List.of("module");
    var text = "lol";
    var page = PageRequest.of(0, 10);

    searchProjectHandler.handle(status, keys, modules, text, null, page);

    verify(projectRepository).filterProjects(eq(List.of("STALE")), eq(keys), eq(modules), eq(text),
        anyCollection(), anyCollection(), eq(page));
  }

  @Test
  void shouldCallRepositoryWithResolvedTags() {
    var givenTags = List.of(
        new TagDto(UUID.randomUUID(), "tag1", Instant.now(), Instant.now())
    );
    var givenTagCollections = List.of(
        new TagCollectionDto(UUID.randomUUID(), givenTags)
    );
    var givenTagCollectionIds = givenTagCollections.stream().map(TagCollectionDto::id).toList();
    when(tagFacade.getTagsByName(anyCollection())).thenReturn(givenTags);
    when(tagCollectionFacade.findWithAllTags(anyCollection())).thenReturn(givenTagCollections);

    searchProjectHandler.handle(null, null, null, null, List.of("tag1"), Pageable.unpaged());

    verify(projectRepository).filterProjects(any(), any(), any(), any(),
        assertArg(ids -> assertThat(ids).containsExactlyElementsOf(givenTagCollectionIds)), any(), any());
  }

  @Test
  void shouldCallRepositoryWithResolvedProfiles() {
    var givenProfiles = List.of(
        new UserProfileDto(UUID.randomUUID(), "Xavier Tester",
            null,
            "",
            true,
            new LecturerProfileDto(
                new LecturerProfileInformationDto(
                    "Test",
                    "Test",
                    List.of(),
                    "",
                    "",
                    ""
                )
            ),
            new ContactInformationDto(
                "",
                "",
                ""
            ),
            List.of())
    );
    var searchQuery = "test";
    when(userProfileFacade.search(eq(searchQuery))).thenReturn(givenProfiles);

    searchProjectHandler.handle(null, null, null, searchQuery, null, Pageable.unpaged());

    verify(projectRepository).filterProjects(any(), any(), any(), any(), anyCollection(),
        assertArg(ids -> assertThat(ids).containsExactly(givenProfiles.get(0).userId())), any());
  }
}