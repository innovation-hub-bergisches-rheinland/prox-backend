package de.innovationhub.prox.modules.tag.application.tag.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MergeTagsRequest(
    @NotNull
    UUID mergeInto
) {

}
