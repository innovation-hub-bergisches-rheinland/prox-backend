package de.innovationhub.prox.modules.organization.contract.event;

import java.util.Collection;
import java.util.UUID;

public record OrganizationTaggedIntegrationEvent(
    UUID organizationId,
    Collection<UUID> tags
) {

}
