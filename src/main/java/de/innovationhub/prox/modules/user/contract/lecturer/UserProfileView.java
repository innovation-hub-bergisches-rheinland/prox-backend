package de.innovationhub.prox.modules.user.contract.lecturer;

import java.io.Serializable;
import java.util.UUID;

public record UserProfileView(
    UUID id,
    String displayName
) implements Serializable {

}
