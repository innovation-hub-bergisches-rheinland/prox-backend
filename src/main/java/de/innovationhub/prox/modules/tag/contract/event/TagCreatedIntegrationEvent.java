package de.innovationhub.prox.modules.tag.contract.event;

import java.util.UUID;

public record TagCreatedIntegrationEvent(
    UUID id,
    String tagName
) {

}
