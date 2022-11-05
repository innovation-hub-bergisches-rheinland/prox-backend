package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;

public record CreateLecturer(
    String name
) implements UseCase {}
