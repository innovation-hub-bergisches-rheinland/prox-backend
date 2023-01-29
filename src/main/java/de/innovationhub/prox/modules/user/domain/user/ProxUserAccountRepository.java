package de.innovationhub.prox.modules.user.domain.user;

import de.innovationhub.prox.commons.buildingblocks.ReadOnlyRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProxUserAccountRepository extends ReadOnlyRepository<ProxUser, UUID> {

  <S extends ProxUser> List<S> search(String query);

  <S extends ProxUser> List<S> searchWithRole(String query, String role);
}
