package de.innovationhub.prox.modules.project.application.project.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.modules.project.application.discipline.web.dto.ReadDisciplineDto;
import de.innovationhub.prox.modules.project.application.module.web.dto.ReadModuleTypeDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ApplyCommitmentDto(
    UUID supervisorId
) { }
