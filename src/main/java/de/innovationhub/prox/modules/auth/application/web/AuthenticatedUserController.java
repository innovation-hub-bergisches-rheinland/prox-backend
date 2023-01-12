package de.innovationhub.prox.modules.auth.application.web;

import de.innovationhub.prox.modules.auth.application.usecase.commands.StarProjectHandler;
import de.innovationhub.prox.modules.auth.application.usecase.commands.UnstarProjectHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class AuthenticatedUserController {
  private final StarProjectHandler starProject;
  private final UnstarProjectHandler unstarProject;


  @PutMapping(value = "stars/projects/{projectId}", produces = "application/json")
  public ResponseEntity<Void> starProject(@PathVariable("projectId") UUID projectId) {
    starProject.handle(getUserId(), projectId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "stars/projects/{projectId}", produces = "application/json")
  public ResponseEntity<Void> unstarProject(@PathVariable("projectId") UUID projectId) {
    unstarProject.handle(getUserId(), projectId);
    return ResponseEntity.noContent().build();
  }

  private UUID getUserId(Authentication authentication) {
    return UUID.fromString(authentication.getName());
  }

  private UUID getUserId() {
    return getUserId(SecurityContextHolder.getContext().getAuthentication());
  }
}
