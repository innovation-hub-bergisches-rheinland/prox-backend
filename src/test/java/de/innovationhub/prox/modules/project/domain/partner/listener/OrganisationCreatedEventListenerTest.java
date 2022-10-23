package de.innovationhub.prox.modules.project.domain.partner.listener;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationCreated;
import de.innovationhub.prox.modules.project.domain.partner.PartnerRepository;
import de.innovationhub.prox.modules.project.domain.partner.events.PartnerCreated;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@Transactional
@RecordApplicationEvents
class OrganisationCreatedEventListenerTest {
  @Autowired
  ApplicationEventPublisher eventPublisher;

  @Autowired
  private ApplicationEvents applicationEvents;

  @Autowired
  PartnerRepository partnerRepository;

  @Test
  void shouldCreatePartner() {
    var orgId = UUID.randomUUID();
    var name = "ACME Ltd";
    var event = new OrganizationCreated(orgId, name, List.of());

    eventPublisher.publishEvent(event);

    var createdPartnerEvent = applicationEvents.stream(PartnerCreated.class)
        .findFirst()
        .get();

    var partner = partnerRepository.findById(createdPartnerEvent.id());
    assertThat(partner)
        .isPresent()
        .get()
        .extracting("name")
        .isEqualTo(name);
  }
}