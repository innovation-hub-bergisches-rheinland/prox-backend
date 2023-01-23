package de.innovationhub.prox.modules.project.application.project.dto;

public record ProjectPermissions(
    boolean hasAccess,
    boolean canStateInterest
) {
}
