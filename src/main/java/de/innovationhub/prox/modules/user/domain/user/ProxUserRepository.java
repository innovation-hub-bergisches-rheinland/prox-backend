package de.innovationhub.prox.modules.user.domain.user;

import de.innovationhub.prox.modules.commons.domain.ReadOnlyRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProxUserRepository extends ReadOnlyRepository<ProxUser, UUID> {

  List<ProxUser> search(String query);
}
