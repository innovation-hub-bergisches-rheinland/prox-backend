package de.innovationhub.prox.modules.project.domain.supervisor.listener;

import de.innovationhub.prox.modules.commons.domain.DomainComponent;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerCreated;
import de.innovationhub.prox.modules.project.domain.supervisor.Supervisor;
import de.innovationhub.prox.modules.project.domain.supervisor.SupervisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@DomainComponent
@RequiredArgsConstructor
public class LecturerCreatedEventListener {
  private final SupervisorRepository supervisorRepository;

  @EventListener
  public void onLecturerCreated(LecturerCreated lecturerCreated) {
    var supervisor = Supervisor.create(lecturerCreated.id(), lecturerCreated.name());
    supervisorRepository.save(supervisor);
  }
}
