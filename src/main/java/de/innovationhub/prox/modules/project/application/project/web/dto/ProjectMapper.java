package de.innovationhub.prox.modules.project.application.project.dto;

import de.innovationhub.prox.modules.profile.contract.LecturerView;
import de.innovationhub.prox.modules.profile.contract.OrganizationView;
import de.innovationhub.prox.modules.project.application.discipline.web.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.application.module.web.dto.ModuleTypeMapper;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.tag.contract.TagCollectionView;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
    ModuleTypeMapper.class,
    DisciplineMapper.class
})
interface ProjectMapper {

  @Mapping(target = ".", source = "project")
  @Mapping(target = "id", source = "project.id")
  @Mapping(target = "tags", source = "tagCollectionView.tags")
  @Mapping(target = "partner", source = "organizationView")
  @Mapping(target = "supervisors", source = "lecturerView")
  ReadProjectDto toDto(Project project, List<LecturerView> lecturerView,
      OrganizationView organizationView, TagCollectionView tagCollectionView);
}
