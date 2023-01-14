package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.TagView;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class SearchLecturerHandlerTestProfile {

  LecturerProfileRepository lecturerRepository = mock(LecturerProfileRepository.class);
  TagFacade tagFacade = mock(TagFacade.class);
  SearchLecturerHandler handler = new SearchLecturerHandler(lecturerRepository, tagFacade);

  @Test
  void shouldCallRepository() {
    var query = "lol";
    var page = PageRequest.of(0, 10);

    handler.handle(query, null, page);

    verify(lecturerRepository).search(eq(query), anyCollection(), eq(page));
  }

  @Test
  void shouldCallRepositoryWithResolvedTags() {
    var givenTags = List.of(
        new TagView(UUID.randomUUID(), "tag1")
    );
    when(tagFacade.getTagsByName(anyCollection())).thenReturn(givenTags);

    handler.handle(null, List.of("tag1"), Pageable.unpaged());

    ArgumentCaptor<List<UUID>> captor = ArgumentCaptor.forClass((Class) List.class);
    verify(lecturerRepository).search(any(), captor.capture(), any());
    assertThat(captor.getValue()).containsExactly(givenTags.get(0).id());
  }
}