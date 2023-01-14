package de.innovationhub.prox.modules.user.application.lecturer.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.contract.user.UserFacade;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileRepository;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class CreateLecturerHandler {

  private final LecturerProfileRepository lecturerRepository;
  private final UserFacade userFacade;

  @Transactional
  public LecturerProfile handle(UUID userId) {
    Objects.requireNonNull(userId);
    var user = userFacade.findById(userId).orElseThrow();

    var lecturer = LecturerProfile.create(userId, user.name());

    var profile = new LecturerProfileInformation();
    profile.setEmail(user.email());
    lecturer.setProfile(profile);

    lecturer.setVisibleInPublicSearch(false);

    return lecturerRepository.save(lecturer);
  }
}
