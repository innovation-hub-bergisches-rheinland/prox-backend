package de.innovationhub.prox.modules.project.application.project.web.dto;

import java.time.LocalDate;

public record TimeBoxDto(
    LocalDate start,
    LocalDate end
) {

}
