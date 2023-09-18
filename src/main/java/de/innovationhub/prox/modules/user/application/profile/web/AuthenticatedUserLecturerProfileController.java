package de.innovationhub.prox.modules.user.application.profile.web;

import static de.innovationhub.prox.utils.SecurityUtils.extractUserId;

import de.innovationhub.prox.modules.user.application.profile.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDtoMapper;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.SetLecturerProfileHandler;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/profile/lecturer")
@SecurityRequirement(name = "oidc")
@RequiredArgsConstructor
@Tag(name = "User Profile")
public class AuthenticatedUserLecturerProfileController {

  private final UserProfileDtoMapper dtoMapper;
  private final SetLecturerProfileHandler updateLecturerProfile;

  @PutMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<UserProfileDto> updateLecturerProfile(
      @RequestBody CreateLecturerRequestDto requestDto,
      Authentication authentication) {
    var result = updateLecturerProfile.handle(extractUserId(authentication), requestDto);
    return ResponseEntity.ok(dtoMapper.toDtoUserProfile(result));
  }
}
