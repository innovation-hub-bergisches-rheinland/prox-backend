package de.innovationhub.prox.modules.profile.application.lecturer.web.dto;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.profile.application.lecturer.LecturerPermissionEvaluator;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
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

  public LecturerDto toDto(Lecturer lecturer) {
    String avatarUrl = null;

    if(lecturer.getAvatarKey() != null) {
      avatarUrl = storageProvider.buildUrl(lecturer.getAvatarKey());
    }

    List<String> tags = Collections.emptyList();
    if (lecturer.getTags() != null) {
      tags = tagFacade.getTags(lecturer.getTags());
    }

    var permissions = new LecturerPermissions(
        lecturerPermissionEvaluator.hasPermission(lecturer, authenticationFacade.getAuthentication())
    );

    return lecturerDtoMapper.toDto(lecturer, tags, avatarUrl, permissions);
  }
}
