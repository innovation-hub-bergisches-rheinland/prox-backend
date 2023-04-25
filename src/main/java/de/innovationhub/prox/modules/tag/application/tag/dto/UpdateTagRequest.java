package de.innovationhub.prox.modules.tag.application.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record UpdateTagRequest(
    String tagName,
    @NotNull
    Set<@NotBlank String> aliases
) {
  public UpdateTagRequest {

  }
}
