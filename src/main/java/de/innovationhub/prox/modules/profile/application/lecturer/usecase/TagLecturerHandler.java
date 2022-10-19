package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.commons.core.NotImplementedYetException;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerTagsDto;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class TagLecturerHandler implements UseCaseHandler<LecturerTagsDto, TagLecturer> {

  @Override
  public LecturerTagsDto handle(TagLecturer useCase) {
    throw new NotImplementedYetException();
  }
}
