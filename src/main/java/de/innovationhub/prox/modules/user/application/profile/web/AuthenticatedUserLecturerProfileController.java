package de.innovationhub.prox.modules.user.application.profile.web;

import de.innovationhub.prox.modules.user.application.profile.usecase.commands.CreateLecturerProfileHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.CreateUserProfileHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.SetLecturerTagsHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.SetUserProfileAvatarHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.UpdateLecturerProfileHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.UpdateUserProfileHandler;
import de.innovationhub.prox.modules.user.application.profile.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.user.application.profile.web.dto.SetLecturerTagsRequestDto;
import de.innovationhub.prox.modules.user.application.profile.web.dto.SetLecturerTagsResponseDto;
import de.innovationhub.prox.modules.user.application.profile.web.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.application.profile.web.dto.UserProfileDtoMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
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
  private final CreateUserProfileHandler createUserProfile;
  private final UpdateUserProfileHandler updateUserProfileHandler;
  private final UserProfileDtoMapper dtoMapper;
  private final CreateLecturerProfileHandler createLecturerProfile;
  private final UpdateLecturerProfileHandler updateLecturerProfile;
  private final SetLecturerTagsHandler setLecturerTags;
  private final SetUserProfileAvatarHandler setAvatar;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<UserProfileDto> createLecturerProfile(
      @RequestBody CreateLecturerRequestDto requestDto,
      Authentication authentication) {
    var result = createLecturerProfile.handle(extractUserId(authentication), requestDto);
    return ResponseEntity.status(201).body(dtoMapper.toDtoUserProfile(result));
  }

  @PutMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<UserProfileDto> updateLecturerProfile(
      @RequestBody CreateLecturerRequestDto requestDto,
      Authentication authentication) {
    var result = updateLecturerProfile.handle(extractUserId(authentication), requestDto);
    return ResponseEntity.ok(dtoMapper.toDtoUserProfile(result));
  }

  @PutMapping(value = "tags", consumes = "application/json", produces = "application/json")
  public ResponseEntity<SetLecturerTagsResponseDto> setLecturerTags(
      @RequestBody SetLecturerTagsRequestDto tagsDto,
      Authentication authentication) {
    var result = setLecturerTags.handle(extractUserId(authentication), tagsDto.tags());
    return ResponseEntity.ok(new SetLecturerTagsResponseDto(dtoMapper.retrieveTags(result)));
  }

  private UUID extractUserId(Authentication authentication) {
    return UUID.fromString(authentication.getName());
  }
}
