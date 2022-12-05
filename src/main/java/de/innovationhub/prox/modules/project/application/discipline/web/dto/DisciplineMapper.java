package de.innovationhub.prox.modules.project.application.discipline.web.dto;

import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper
public interface DisciplineMapper {
    ReadDisciplineDto toDto(Discipline discipline);

    default Page<ReadDisciplineDto> toDtoPage(Page<Discipline> disciplines) {
      return disciplines.map(this::toDto);
    }
}
