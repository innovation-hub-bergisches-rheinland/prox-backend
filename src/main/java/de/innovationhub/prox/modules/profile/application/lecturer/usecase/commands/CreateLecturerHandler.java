package de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerProfileInformation;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.user.contract.user.UserFacade;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class CreateLecturerHandler {

  private final LecturerRepository lecturerRepository;
  private final UserFacade userFacade;

  @Transactional
  public Lecturer handle(UUID userId) {
    Objects.requireNonNull(userId);
    var user = userFacade.findById(userId).orElseThrow();

    var lecturer = Lecturer.create(userId, user.name());

    var profile = new LecturerProfileInformation();
    profile.setEmail(user.email());
    lecturer.setProfile(profile);

    lecturer.setVisibleInPublicSearch(false);

    return lecturerRepository.save(lecturer);
  }
}
