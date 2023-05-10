package de.innovationhub.prox.modules.project.application.discipline.dto;

import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface DisciplineMapper {
    DisciplineDto toDto(Discipline discipline);
    List<DisciplineDto> toDtoList(List<Discipline> disciplines);
}
