package de.innovationhub.prox.modules.project.application.project.web.dto;

import java.util.List;

public record CurriculumContextRequest(
    List<String> moduleTypeKeys,
    List<String> disciplineKeys
) {

}
