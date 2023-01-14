package de.innovationhub.prox.modules.user.application.lecturer.web.dto;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.user.application.lecturer.LecturerPermissionEvaluator;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfile;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LecturerDtoAssembler {
  private final TagFacade tagFacade;
  private final LecturerDtoMapper lecturerDtoMapper;
  private final StorageProvider storageProvider;

  // TODO: EXPERIMENTAL
  private final LecturerPermissionEvaluator lecturerPermissionEvaluator;
  private final AuthenticationFacade authenticationFacade;

  public LecturerDto toDto(LecturerProfile lecturerProfile) {
    String avatarUrl = null;

    if (lecturerProfile.getAvatarKey() != null) {
      avatarUrl = storageProvider.buildUrl(lecturerProfile.getAvatarKey());
    }

    List<String> tags = Collections.emptyList();
    if (lecturerProfile.getTags() != null) {
      tags = tagFacade.getTags(lecturerProfile.getTags());
    }

    var permissions = new LecturerPermissions(
        lecturerPermissionEvaluator.hasPermission(lecturerProfile,
            authenticationFacade.getAuthentication())
    );

    return lecturerDtoMapper.toDto(lecturerProfile, tags, avatarUrl, permissions);
  }
}
