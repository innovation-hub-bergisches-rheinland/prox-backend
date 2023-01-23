package de.innovationhub.prox.modules.user.application.profile;

import de.innovationhub.prox.modules.user.application.profile.dto.CreateLecturerRequestDto.CreateLecturerProfileDto;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserProfileMapper {
  public static final UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);
  void updateLecturerInformationFromDto(CreateLecturerProfileDto dto, @MappingTarget LecturerProfileInformation information);
}
