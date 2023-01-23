package de.innovationhub.prox.modules.tag.application.tag.dto;

import java.util.List;

public record SynchronizeTagsRequest(
    List<String> tags
) {

}
