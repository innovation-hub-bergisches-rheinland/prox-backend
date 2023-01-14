package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.TagView;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchLecturerHandler {

  private final LecturerProfileRepository lecturerRepository;
  private final TagFacade tagFacade;

  public Page<LecturerProfile> handle(
      String query,
      Collection<String> tags,
      Pageable pageable) {
    List<UUID> tagIds = new ArrayList<>();
    if (tags != null) {
      tagIds.addAll(
          tagFacade.getTagsByName(tags).stream().map(TagView::id).toList()
      );
    }

    return lecturerRepository.search(query, tagIds, pageable);
  }
}
