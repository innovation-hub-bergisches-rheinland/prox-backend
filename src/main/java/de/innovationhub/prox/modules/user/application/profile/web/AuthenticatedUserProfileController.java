package de.innovationhub.prox.modules.user.application.profile.web;

import static de.innovationhub.prox.utils.SecurityUtils.extractUserId;

import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.SetLecturerTagsResponseDto;
import de.innovationhub.prox.modules.user.application.profile.dto.SetTagsRequestDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDtoMapper;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.SetUserProfileAvatarHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.SetUserProfileTagsHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.UpdateUserProfileHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindUserProfileHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user/profile")
@SecurityRequirement(name = "oidc")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Endpoints for managing user profiles")
public class AuthenticatedUserProfileController {
  private final FindUserProfileHandler findUserProfileHandler;
  private final UpdateUserProfileHandler updateUserProfileHandler;
  private final UserProfileDtoMapper dtoMapper;
  private final SetUserProfileAvatarHandler setAvatar;
  private final SetUserProfileTagsHandler setTags;

  @GetMapping(produces = "application/json")
  public ResponseEntity<UserProfileDto> get(Authentication authentication) {
    return findUserProfileHandler.handle(extractUserId(authentication))
        .map(dtoMapper::toDtoUserProfile)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(404).build());
  }

  @PutMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<UserProfileDto> update(@RequestBody CreateUserProfileRequestDto requestDto, Authentication authentication) {
    var up = updateUserProfileHandler.handle(extractUserId(authentication), requestDto);
    return ResponseEntity.ok(dtoMapper.toDtoUserProfile(up));
  }

  @PutMapping(value = "tags", consumes = "application/json", produces = "application/json")
  public ResponseEntity<SetLecturerTagsResponseDto> setLecturerTags(
      @RequestBody SetTagsRequestDto tagsDto,
      Authentication authentication) {
    var result = setTags.handle(extractUserId(authentication), tagsDto.tags());
    return ResponseEntity.ok(new SetLecturerTagsResponseDto(result));
  }

  @PostMapping(value = "avatar", consumes = "multipart/form-data")
  public ResponseEntity<?> postAvatar(
      @RequestParam("image") MultipartFile multipartFile,
      Authentication authentication
  ) throws IOException {
    if(multipartFile.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    var contentType = multipartFile.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
    }
    setAvatar.handle(extractUserId(authentication), multipartFile.getBytes(), contentType);
    return ResponseEntity.noContent().build();
  }
}
