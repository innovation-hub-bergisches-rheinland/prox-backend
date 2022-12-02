package de.innovationhub.prox.modules.profile.application.lecturer.web.dto;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
interface LecturerDtoMapper {

  LecturerDtoMapper INSTANCE = Mappers.getMapper(LecturerDtoMapper.class);

  @Mapping(target = "userId", source = "lecturer.userId")
  @Mapping(target = "tags", source = "tags")
  @Mapping(target = "avatarUrl", source = "avatarUrl")
  ReadLecturerDto toDto(Lecturer lecturer, List<String> tags, String avatarUrl, LecturerPermissions permissions);
}
