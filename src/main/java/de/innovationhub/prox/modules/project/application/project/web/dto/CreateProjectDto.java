package de.innovationhub.prox.modules.project.application.project.web.dto;


public record CreateProjectDto(
    String title,
    String description,
    String summary,
    String requirement,
    CurriculumContextDto context,
    TimeBoxDto timeBox) {
}
