package de.innovationhub.prox.modules.user.domain.account;

import de.innovationhub.prox.modules.commons.domain.ReadOnlyRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProxUserAccountRepository extends ReadOnlyRepository<ProxUserAccount, UUID> {

  <S extends ProxUserAccount> List<S> search(String query);
}
