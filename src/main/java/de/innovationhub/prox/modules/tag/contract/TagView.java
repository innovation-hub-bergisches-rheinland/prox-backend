package de.innovationhub.prox.modules.tag.contract;

import java.util.UUID;

public record TagView(
    UUID id,
    String tagName
) {

}
