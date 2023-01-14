package de.innovationhub.prox.modules.user.contract.lecturer;

import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfile;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface LecturerViewMapper {

  LecturerView toView(LecturerProfile lecturerProfile);

  List<LecturerView> toViewList(List<LecturerProfile> lecturerProfiles);
}
