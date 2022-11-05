package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;

public record CreateOrganization(
    String name
) implements UseCase {

}
