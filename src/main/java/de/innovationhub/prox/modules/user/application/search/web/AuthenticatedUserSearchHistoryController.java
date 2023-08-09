package de.innovationhub.prox.modules.user.application.search.web;

import static de.innovationhub.prox.utils.SecurityUtils.extractUserId;

import de.innovationhub.prox.modules.user.application.search.dto.ReadSearchHistoryDto;
import de.innovationhub.prox.modules.user.application.search.dto.SearchHistoryDtoMapper;
import de.innovationhub.prox.modules.user.application.search.usecase.queries.GetSearchHistoryHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/searchHistory")
@SecurityRequirement(name = "oidc")
@RequiredArgsConstructor
public class AuthenticatedUserSearchHistoryController {

  private final GetSearchHistoryHandler getSearchHistoryHandler;
  private final SearchHistoryDtoMapper dtoMapper;

  @GetMapping(produces = "application/json")
  public ResponseEntity<ReadSearchHistoryDto> get(Authentication authentication) {
    return getSearchHistoryHandler.handle(extractUserId(authentication))
        .map(dtoMapper::toReadSearchHistoryDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(404).build());
  }
}
