package de.innovationhub.prox.modules.user.application.profile.web;

import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindUserProfileHandler;
import de.innovationhub.prox.modules.user.application.profile.web.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.application.profile.web.dto.UserProfileDtoMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "User Profile")
public class UserProfileController {
  private final FindUserProfileHandler findUserProfile;
  private final UserProfileDtoMapper userProfileDtoMapper;

  @GetMapping(value = "{id}", produces = "application/json")
  public ResponseEntity<UserProfileDto> findProfile(@PathVariable("id") UUID userId) {
    return findUserProfile.handle(userId)
        .map(userProfileDtoMapper::toDtoUserProfile)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
