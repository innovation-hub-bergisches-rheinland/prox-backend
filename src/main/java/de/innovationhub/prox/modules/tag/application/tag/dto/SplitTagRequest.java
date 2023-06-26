package de.innovationhub.prox.modules.tag.application.tag.dto;

import java.util.List;

public record SplitTagRequest(
    List<String> splitInto
) {

}
