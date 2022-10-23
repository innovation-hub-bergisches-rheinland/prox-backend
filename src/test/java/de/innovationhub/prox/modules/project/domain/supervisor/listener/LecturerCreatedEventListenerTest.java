package de.innovationhub.prox.modules.project.domain.supervisor.listener;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerCreated;
import de.innovationhub.prox.modules.project.domain.supervisor.SupervisorRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
@Transactional
class LecturerCreatedEventListenerTest {
  @Autowired
  ApplicationEventPublisher eventPublisher;

  @Autowired
  SupervisorRepository supervisorRepository;

  @Test
  void shouldCreateSupervisor() {
    var lecturerId = UUID.randomUUID();
    var userId = UUID.randomUUID();
    var name = "Xavier Tester";
    var event = new LecturerCreated(lecturerId, userId, name);

    eventPublisher.publishEvent(event);

    var supervisor = supervisorRepository.findById(lecturerId);
    assertThat(supervisor)
        .isPresent()
        .get()
        .extracting("name")
        .isEqualTo(name);
  }
}