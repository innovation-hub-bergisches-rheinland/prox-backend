package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import java.util.List;
import java.util.UUID;

public record TagLecturer(
    UUID lecturerId,
    List<String> tags
) implements UseCase {}