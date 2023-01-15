package de.innovationhub.prox.modules.user.contract.lecturer;

import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface UserProfileViewMapper {

  UserProfileView toView(UserProfile userProfile);

  List<UserProfileView> toViewList(List<UserProfile> userProfiles);
}
