package de.innovationhub.prox.modules.auth.web;

import de.innovationhub.prox.modules.auth.contract.UserFacade;
import de.innovationhub.prox.modules.auth.contract.UserView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
  private final UserFacade userFacade;

  @GetMapping("search")
  public List<UserView> find(@RequestParam("q") String searchQuery) {
    return userFacade.search(searchQuery);
  }
}
