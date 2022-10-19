package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import java.util.UUID;

public record CreateLecturer(
    UUID userId,
    String name
) implements UseCase {}
