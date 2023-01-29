package de.innovationhub.prox.commons.buildingblocks;

import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {

    <S extends T> Optional<S> findById(ID id);

    boolean existsById(ID id);

    long count();
}
