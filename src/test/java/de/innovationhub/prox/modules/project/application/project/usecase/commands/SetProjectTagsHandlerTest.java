package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.organization.OrganizationFixtures;
import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.SetProjectTagsHandler;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetProjectTagsHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  TagCollectionFacade tagCollectionFacade = mock(TagCollectionFacade.class);
  SetProjectTagsHandler setProjectTagsHandler = new SetProjectTagsHandler(projectRepository, tagCollectionFacade);

  @Test
  void shouldThrowWhenProjectNotFound() {
    var id = UUID.randomUUID();
    when(projectRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> setProjectTagsHandler.handle(id, List.of()))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldSaveWithTags() {
    var project = ProjectFixtures.build_a_project();
    var tags = Instancio.ofList(TagDto.class).size(3).create();
    var tagIds = tags.stream().map(TagDto::id).toList();
    var tagCollectionId = project.getTagCollectionId();

    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
    when(tagCollectionFacade.setTagCollection(any(), any())).thenReturn(new TagCollectionDto(tagCollectionId, tags));

    setProjectTagsHandler.handle(project.getId(), tagIds);

    verify(projectRepository).save(assertArg(o -> assertThat(o.getTagCollectionId()).isEqualTo(tagCollectionId)));
    verify(tagCollectionFacade).setTagCollection(eq(tagCollectionId), assertArg(t -> assertThat(t).containsExactlyElementsOf(tagIds)));
  }
}