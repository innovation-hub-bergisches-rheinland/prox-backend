package de.innovationhub.prox.modules.user.application.user.web.dto;

import de.innovationhub.prox.modules.user.domain.user.ProxUser;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface ProxUserMapper {
  ProxUserDto toDto(ProxUser proxUser);
  List<ProxUserDto> toDto(List<ProxUser> proxUsers);
}
