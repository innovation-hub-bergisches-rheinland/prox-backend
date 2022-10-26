package de.innovationhub.prox.modules.project.application.project.dto;

import de.innovationhub.prox.modules.profile.contract.LecturerView;
import de.innovationhub.prox.modules.profile.contract.OrganizationView;
import de.innovationhub.prox.modules.project.application.discipline.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.application.module.dto.ModuleTypeMapper;
import de.innovationhub.prox.modules.project.application.project.dto.ReadProjectDto.AuthorDto;
import de.innovationhub.prox.modules.project.application.project.dto.ReadProjectDto.CurriculumContextDto;
import de.innovationhub.prox.modules.project.application.project.dto.ReadProjectDto.PartnerDto;
import de.innovationhub.prox.modules.project.application.project.dto.ReadProjectDto.ProjectStatusDto;
import de.innovationhub.prox.modules.project.application.project.dto.ReadProjectDto.SupervisorDto;
import de.innovationhub.prox.modules.project.application.project.dto.ReadProjectDto.TimeBoxDto;
import de.innovationhub.prox.modules.project.domain.project.Author;
import de.innovationhub.prox.modules.project.domain.project.CurriculumContext;
import de.innovationhub.prox.modules.project.domain.project.Partner;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectStatus;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import de.innovationhub.prox.modules.project.domain.project.TimeBox;
import de.innovationhub.prox.modules.tag.contract.TagCollectionView;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
    ModuleTypeMapper.class,
    DisciplineMapper.class
})
public interface ProjectMapper {
//  @Mapping(target = ".", source = "project")
//  @Mapping(target = "id", source = "project.id")
//  @Mapping(target = "tags", source = "tagCollectionView.tags")
//  ReadProjectDto toDto(Project project, List<LecturerView> lecturerView, OrganizationView organizationView, TagCollectionView tagCollectionView);
//  AuthorDto toDto(Author author);
//  CurriculumContextDto toDto(CurriculumContext curriculumContext);
//  PartnerDto toDto(OrganizationView organizationView);
//  ProjectStatusDto toDto(ProjectStatus status);
//  SupervisorDto toDto(LecturerView supervisor);
//  List<SupervisorDto> toDtoList(List<LecturerView> supervisors);
//  TimeBoxDto toDto(TimeBox timeBox);
}
