package de.innovationhub.prox.modules.user.application.search.web;

import de.innovationhub.prox.modules.user.application.search.dto.CreateSearchPreferencesRequest;
import de.innovationhub.prox.modules.user.application.search.usecase.commands.CreateSearchPreferencesHandler;
import de.innovationhub.prox.modules.user.application.search.usecase.commands.UpdateSearchPreferencesHandler;
import de.innovationhub.prox.modules.user.application.search.usecase.queries.FindSearchPreferencesByUserIdHandler;
import de.innovationhub.prox.modules.user.contract.search.dto.SearchPreferencesDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/search")
@SecurityRequirement(name = "oidc")
@RequiredArgsConstructor
@Tag(name = "User Search", description = "Endpoints for managing user search preferences")
public class AuthenticatedSearchPreferencesController {
  private final FindSearchPreferencesByUserIdHandler findSearchPreferencesByUserIdHandler;
  private final CreateSearchPreferencesHandler createSearchPreferencesHandler;
  private final UpdateSearchPreferencesHandler updateSearchPreferencesHandler;

  @GetMapping
  public ResponseEntity<SearchPreferencesDto> get(Authentication authentication) {
    return findSearchPreferencesByUserIdHandler.handle(extractUserId(authentication))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(404).build());
  }

  @PostMapping
  public ResponseEntity<SearchPreferencesDto> create(
      @Valid @RequestBody CreateSearchPreferencesRequest request,
      Authentication authentication
  ) {
    var dto = createSearchPreferencesHandler.handle(extractUserId(authentication), request);
    return ResponseEntity.status(201).body(dto);
  }

  @PutMapping
  public ResponseEntity<SearchPreferencesDto> update(
      @Valid @RequestBody CreateSearchPreferencesRequest request,
      Authentication authentication
  ) {
    var dto = updateSearchPreferencesHandler.handle(extractUserId(authentication), request);
    return ResponseEntity.status(200).body(dto);
  }

  private UUID extractUserId(Authentication authentication) {
    return UUID.fromString(authentication.getName());
  }
}
