package de.innovationhub.prox.modules.user.contract.profile;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record UserProfileView(
    UUID id,
    String displayName,
    List<UUID> tags
) implements Serializable {

}
