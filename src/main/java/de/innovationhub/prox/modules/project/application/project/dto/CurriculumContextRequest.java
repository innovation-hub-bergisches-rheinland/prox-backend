package de.innovationhub.prox.modules.project.application.project.dto;

import java.util.List;

public record CurriculumContextRequest(
    List<String> moduleTypeKeys,
    List<String> disciplineKeys
) {

}
