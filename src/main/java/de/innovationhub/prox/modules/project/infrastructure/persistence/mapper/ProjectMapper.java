package de.innovationhub.prox.modules.project.infrastructure.persistence.mapper;

import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.infrastructure.persistence.model.ProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProjectMapper {

  ProjectMapper MAPPER = Mappers.getMapper(ProjectMapper.class);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "creator", source = "creatorId")
  @Mapping(target = "organization", source = "organizationId")
  @Mapping(target = "tags", source = "tags")
  @Mapping(target = "modules", source = "curriculumContext.modules")
  @Mapping(target = "disciplines", source = "curriculumContext.disciplines")
  @Mapping(target = "supervisors", source = "supervisors")
  @Mapping(target = "status.state", source = "status.state")
  @Mapping(target = "status.updatedAt", source = "status.updatedAt")
  @Mapping(target = "timeBoxStart", source = "timeBox.start")
  @Mapping(target = "timeBoxEnd", source = "timeBox.end")
  ProjectEntity toPersistence(Project project);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "creatorId", source = "creator")
  @Mapping(target = "organizationId", source = "organization")
  @Mapping(target = "curriculumContext.modules", source = "modules")
  @Mapping(target = "curriculumContext.disciplines", source = "disciplines")
  @Mapping(target = "supervisors", source = "supervisors")
  @Mapping(target = "status.state", source = "status.state")
  @Mapping(target = "status.updatedAt", source = "status.updatedAt")
  @Mapping(target = "timeBox.start", source = "timeBoxStart")
  @Mapping(target = "timeBox.end", source = "timeBoxEnd")
  Project toDomain(ProjectEntity entity);
}
