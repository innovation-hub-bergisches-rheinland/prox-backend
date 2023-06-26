package de.innovationhub.prox.modules.tag.application.tag.dto;

import java.util.List;
import java.util.UUID;

public record SplitTagRequest(
    List<UUID> splitInto
) {

}
