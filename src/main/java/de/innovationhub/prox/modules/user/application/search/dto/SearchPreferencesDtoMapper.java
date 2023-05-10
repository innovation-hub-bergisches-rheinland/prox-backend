package de.innovationhub.prox.modules.user.application.search.dto;

import de.innovationhub.prox.infra.aws.StorageProvider;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto.ContactInformationRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.LecturerProfileInformationDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.LecturerProfileDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.contract.search.dto.SearchPreferencesDto;
import de.innovationhub.prox.modules.user.contract.search.dto.SearchPreferencesDto.LecturerSearchDto;
import de.innovationhub.prox.modules.user.contract.search.dto.SearchPreferencesDto.OrganizationSearchDto;
import de.innovationhub.prox.modules.user.contract.search.dto.SearchPreferencesDto.ProjectSearchDto;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfile;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.search.LecturerSearch;
import de.innovationhub.prox.modules.user.domain.search.OrganizationSearch;
import de.innovationhub.prox.modules.user.domain.search.ProjectSearch;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferences;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class SearchPreferencesDtoMapper {
  public static final SearchPreferencesDtoMapper INSTANCE = Mappers.getMapper(
      SearchPreferencesDtoMapper.class);

  public abstract SearchPreferencesDto toDto(SearchPreferences searchPreferences);

  public abstract ProjectSearchDto toDto(ProjectSearch projectSearch);
  public abstract LecturerSearchDto toDto(LecturerSearch lecturerSearch);
  public abstract OrganizationSearchDto toDto(OrganizationSearch organizationSearch);
}
