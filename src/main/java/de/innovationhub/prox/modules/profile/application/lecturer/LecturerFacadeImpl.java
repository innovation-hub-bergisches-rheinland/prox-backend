package de.innovationhub.prox.modules.profile.application.lecturer;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.FindLecturer;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.FindLecturerHandler;
import de.innovationhub.prox.modules.profile.contract.LecturerFacade;
import de.innovationhub.prox.modules.profile.contract.LecturerView;
import de.innovationhub.prox.modules.profile.contract.LecturerViewMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class LecturerFacadeImpl implements LecturerFacade {
  private final FindLecturerHandler findLecturerHandler;
  private final LecturerViewMapper lecturerViewMapper;

  @Override
  public LecturerView get(UUID id) {
    return lecturerViewMapper.toView(findLecturerHandler.handle(new FindLecturer(id)));
  }
}
