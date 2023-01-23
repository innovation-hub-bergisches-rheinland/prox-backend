package de.innovationhub.prox.modules.user.application.user.web;

import de.innovationhub.prox.modules.user.application.user.dto.ProxUserDto;
import de.innovationhub.prox.modules.user.application.user.dto.ProxUserMapper;
import de.innovationhub.prox.modules.user.application.user.usecase.queries.SearchUserHandler;
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
public class UserAccountController {

  private final SearchUserHandler search;
  private final ProxUserMapper mapper;

  @GetMapping("search")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public List<ProxUserDto> find(
      @RequestParam("q") String searchQuery,
      @RequestParam(value = "role", required = false) String role
  ) {
    return mapper.toDto(search.handle(searchQuery, role));
  }
}
