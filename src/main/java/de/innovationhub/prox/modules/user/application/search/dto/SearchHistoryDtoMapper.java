package de.innovationhub.prox.modules.user.application.search.dto;

import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.contract.dto.ModuleTypeDto;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.application.search.dto.ReadSearchHistoryDto.ProjectSearchEntryDto;
import de.innovationhub.prox.modules.user.domain.search.ProjectSearchEntry;
import de.innovationhub.prox.modules.user.domain.search.SearchHistory;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class SearchHistoryDtoMapper {

  public static final SearchHistoryDtoMapper INSTANCE = Mappers.getMapper(
      SearchHistoryDtoMapper.class);

  @Autowired
  private TagFacade tagFacade;
  @Autowired
  private ProjectFacade projectFacade;

  public abstract ReadSearchHistoryDto toReadSearchHistoryDto(SearchHistory userProfile);

  @Mapping(target = "tags", source = ".", qualifiedByName = "retrieveTags")
  @Mapping(target = "disciplines", source = ".", qualifiedByName = "retrieveDisciplines")
  @Mapping(target = "moduleTypes", source = ".", qualifiedByName = "retrieveModuleTypes")
  @Mapping(target = "newProjects", source = ".", qualifiedByName = "retrieveNewProjects")
  public abstract ProjectSearchEntryDto toProjectSearchDto(ProjectSearchEntry userProfile);

  @Named("retrieveNewProjects")
  public int retrieveNewProjects(ProjectSearchEntry projectSearchEntry) {
    return projectFacade.countProjects(
        projectSearchEntry.getStates(),
        projectSearchEntry.getDisciplines(),
        projectSearchEntry.getModuleTypes(),
        projectSearchEntry.getText(),
        projectSearchEntry.getTags(),
        projectSearchEntry.getModifiedAt()
    );
  }

  @Named("retrieveTags")
  public List<TagDto> retrieveTags(ProjectSearchEntry projectSearchEntry) {
    return tagFacade.getTags(projectSearchEntry.getTags());
  }

  @Named("retrieveDisciplines")
  public List<DisciplineDto> retrieveDisciplines(ProjectSearchEntry projectSearchEntry) {
    return projectFacade.findDisciplinesByKeyIn(projectSearchEntry.getDisciplines());
  }

  @Named("retrieveModuleTypes")
  public List<ModuleTypeDto> retrieveModuleTypes(ProjectSearchEntry projectSearchEntry) {
    return projectFacade.findModuleTypesByKeyIn(projectSearchEntry.getModuleTypes());
  }
}
