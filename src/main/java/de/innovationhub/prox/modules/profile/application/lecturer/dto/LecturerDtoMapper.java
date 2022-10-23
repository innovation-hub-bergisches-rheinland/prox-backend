package de.innovationhub.prox.modules.profile.application.lecturer.dto;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerProfile;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LecturerDtoMapper {
  LecturerDtoMapper INSTANCE = Mappers.getMapper(LecturerDtoMapper.class);

  @Mapping(target = "userId", source = "user.id")
  LecturerDto toDto(Lecturer lecturer);

  default LecturerTagsDto toTagsDto(Collection<String> tags) {
    return new LecturerTagsDto(List.copyOf(tags));
  }
  LecturerProfileDto toProfileDto(LecturerProfile profile);
}
