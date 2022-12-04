package de.innovationhub.prox.modules.auth.web;

import de.innovationhub.prox.modules.auth.contract.UserFacade;
import de.innovationhub.prox.modules.auth.contract.UserView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {
  private final UserFacade userFacade;

  @GetMapping("search")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public List<UserView> find(@RequestParam("q") String searchQuery) {
    return userFacade.search(searchQuery);
  }
}
