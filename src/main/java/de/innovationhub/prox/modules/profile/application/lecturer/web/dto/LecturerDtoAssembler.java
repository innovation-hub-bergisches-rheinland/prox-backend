package de.innovationhub.prox.modules.profile.application.lecturer.web.dto;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LecturerDtoAssembler {
  private final TagCollectionFacade tagCollectionFacade;
  private final LecturerDtoMapper lecturerDtoMapper;

  public ReadLecturerDto toDto(Lecturer lecturer) {
    if (lecturer.getTags() != null) {
      var tagCollectionView = tagCollectionFacade.get(lecturer.getTags().getTagCollectionId());
      if (tagCollectionView.isPresent()) {
        return lecturerDtoMapper.toDto(lecturer, tagCollectionView.get().tags());
      }
    }

    return lecturerDtoMapper.toDto(lecturer, Collections.emptyList());
  }
}
