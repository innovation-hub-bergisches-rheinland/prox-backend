package de.innovationhub.prox.modules.organization.contract;

import java.io.Serializable;
import java.util.UUID;

public record OrganizationView(
    UUID id,
    String name
) implements Serializable {

}