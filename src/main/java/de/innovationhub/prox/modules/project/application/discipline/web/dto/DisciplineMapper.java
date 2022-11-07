package de.innovationhub.prox.modules.project.application.discipline.web.dto;

import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface DisciplineMapper {
    ReadDisciplineDto toDto(Discipline discipline);
    List<ReadDisciplineDto> toDtoList(List<Discipline> disciplines);
}
