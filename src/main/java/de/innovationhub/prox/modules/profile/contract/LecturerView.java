package de.innovationhub.prox.modules.profile.contract;

import java.io.Serializable;
import java.util.UUID;

public record LecturerView(
    UUID id,
    String displayName
) implements Serializable {

}
