package de.innovationhub.prox.modules.project.application.project.web.dto;


import java.util.List;

public record UpdateProjectDto(
    String title,
    String description,
    String summary,
    String requirement,
    CurriculumContextDto context,
    PartnerDto partner,
    TimeBoxDto timeboxDto,
    List<SupervisorDto> supervisors) {
}
