package de.innovationhub.prox.modules.user.application.search.web;

import de.innovationhub.prox.modules.user.application.search.usecase.queries.FindSearchPreferencesByUserIdHandler;
import de.innovationhub.prox.modules.user.contract.search.dto.SearchPreferencesDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/search")
@SecurityRequirement(name = "oidc")
@RequiredArgsConstructor
@Tag(name = "User Search", description = "Endpoints for managing user search preferences")
public class AuthenticatedSearchPreferencesController {
  private final FindSearchPreferencesByUserIdHandler findSearchPreferencesByUserIdHandler;

  @GetMapping
  public ResponseEntity<SearchPreferencesDto> get(Authentication authentication) {
    return findSearchPreferencesByUserIdHandler.handle(extractUserId(authentication))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(404).build());
  }

  private UUID extractUserId(Authentication authentication) {
    return UUID.fromString(authentication.getName());
  }
}
