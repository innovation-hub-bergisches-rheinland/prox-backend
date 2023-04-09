package de.innovationhub.prox.modules.project.contract;

import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProjectViewMapper {
  @Mapping(target = "partner", source = "partner.organizationId")
  ProjectView toView(Project project);

  default List<UUID> mapSupervisors(List<Supervisor> supervisors) {
    if(supervisors == null) {
      return List.of();
    }
    return supervisors.stream().map(Supervisor::getLecturerId).toList();
  }
}
