package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.core.NotImplementedYetException;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerTagsDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class TagLecturerHandler {

  public LecturerTagsDto handle(UUID lecturerId,
      List<String> tags) {
    throw new NotImplementedYetException();
  }
}
