package de.innovationhub.prox.modules.organization.domain.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import de.innovationhub.prox.modules.organization.domain.Membership;
import de.innovationhub.prox.modules.organization.domain.Organization;
import java.util.List;
import java.util.UUID;

public record OrganizationDeleted(
    UUID id,
    String name
) implements DomainEvent {}