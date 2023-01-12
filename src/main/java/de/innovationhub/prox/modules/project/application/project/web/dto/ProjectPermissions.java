package de.innovationhub.prox.modules.project.application.project.web.dto;

public record ProjectPermissions(
    boolean hasAccess,
    boolean canStateInterest
) {
}
