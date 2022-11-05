package de.innovationhub.prox.modules.profile.application.lecturer.dto;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
interface LecturerDtoMapper {

  LecturerDtoMapper INSTANCE = Mappers.getMapper(LecturerDtoMapper.class);

  @Mapping(target = "userId", source = "lecturer.user.userId")
  @Mapping(target = "tags", source = "tags")
  LecturerDto toDto(Lecturer lecturer, List<String> tags);
}
