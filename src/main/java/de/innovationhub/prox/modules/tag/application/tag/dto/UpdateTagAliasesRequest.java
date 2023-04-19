package de.innovationhub.prox.modules.tag.application.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record UpdateTagAliasesRequest(
    @NotNull
    Set<@NotBlank String> aliases
) {
  public UpdateTagAliasesRequest {

  }
}
