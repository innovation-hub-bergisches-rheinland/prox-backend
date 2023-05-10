package de.innovationhub.prox.modules.user.application.search.dto;

import de.innovationhub.prox.infra.aws.StorageProvider;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.contract.dto.ModuleTypeDto;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class SearchPreferencesDtoMapper {
  public static final SearchPreferencesDtoMapper INSTANCE = Mappers.getMapper(
      SearchPreferencesDtoMapper.class);

  @Autowired
  private TagCollectionFacade tagCollectionFacade;

  @Autowired
  private ProjectFacade projectFacade;

  @Mapping(target = "tags", source = ".", qualifiedByName = "retrieveTags")
  public abstract SearchPreferencesDto toDto(SearchPreferences searchPreferences);


  @Mapping(target = "moduleTypes", source = ".", qualifiedByName = "retrieveModuleTypes")
  @Mapping(target = "disciplines", source = ".", qualifiedByName = "retrieveDisciplines")
  public abstract ProjectSearchDto toDto(ProjectSearch projectSearch);
  public abstract LecturerSearchDto toDto(LecturerSearch lecturerSearch);
  public abstract OrganizationSearchDto toDto(OrganizationSearch organizationSearch);

  @Named("retrieveTags")
  public List<TagDto> retrieveTags(SearchPreferences searchPreferences) {
    if(searchPreferences == null || searchPreferences.getTagCollectionId() == null) {
      return List.of();
    }
    return tagCollectionFacade.getTagCollection(searchPreferences.getTagCollectionId()).map(
        TagCollectionDto::tags).orElse(List.of());
  }

  @Named("retrieveModuleTypes")
  public Set<ModuleTypeDto> retrieveModuleTypes(ProjectSearch ps) {
    if(ps == null || ps.getModuleTypes() == null) {
      return Set.of();
    }
    return new HashSet<>(projectFacade.findModuleTypesByKeyIn(ps.getModuleTypes()));
  }

  @Named("retrieveDisciplines")
  public Set<DisciplineDto> retrieveDisciplines(ProjectSearch ps) {
    if(ps == null || ps.getDisciplines() == null) {
      return Set.of();
    }
    return new HashSet<>(projectFacade.findDisciplinesByKeyIn(ps.getDisciplines()));
  }
}
