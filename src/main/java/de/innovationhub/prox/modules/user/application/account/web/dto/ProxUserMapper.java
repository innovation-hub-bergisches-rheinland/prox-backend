package de.innovationhub.prox.modules.user.application.account.web.dto;

import de.innovationhub.prox.modules.user.domain.account.ProxUserAccount;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface ProxUserMapper {

  ProxUserDto toDto(ProxUserAccount proxUser);

  List<ProxUserDto> toDto(List<ProxUserAccount> proxUsers);
}
