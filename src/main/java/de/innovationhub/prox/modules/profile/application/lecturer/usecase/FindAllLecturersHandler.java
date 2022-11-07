package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindAllLecturersHandler {

  private final LecturerRepository lecturerRepository;

  public List<Lecturer> handle() {
    return StreamSupport.stream(lecturerRepository.findAll().spliterator(), false).toList();
  }
}
