package de.innovationhub.prox.modules.star.application.web;

import de.innovationhub.prox.modules.star.application.usecase.commands.StarProjectHandler;
import de.innovationhub.prox.modules.star.application.usecase.commands.UnstarProjectHandler;
import de.innovationhub.prox.modules.star.application.usecase.queries.CheckProjectStarHandler;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "Star", description = "Star API")
public class AuthenticatedUserStarController {

  private final StarProjectHandler starProject;
  private final UnstarProjectHandler unstarProject;
  private final CheckProjectStarHandler checkProjectStar;


  @GetMapping(value = "stars/projects/{projectId}", produces = "application/json")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Project starred"),
      @ApiResponse(responseCode = "404", description = "Project not starred"),
  })
  public ResponseEntity<Void> checkStar(@PathVariable("projectId") UUID projectId) {
    var result = checkProjectStar.handle(getUserId(), projectId);
    if (result) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

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