package de.innovationhub.prox.modules.project.contract.dto;

public record ProjectPermissions(
    boolean hasAccess,
    boolean canStateInterest
) {
}
