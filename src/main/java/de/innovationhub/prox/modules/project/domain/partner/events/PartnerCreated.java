package de.innovationhub.prox.modules.project.domain.partner.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.project.domain.partner.Partner;
import java.util.UUID;

public record PartnerCreated(UUID id, String name) implements Event {
  public static PartnerCreated from(Partner partner) {
    return new PartnerCreated(partner.getId(), partner.getName());
  }
}
