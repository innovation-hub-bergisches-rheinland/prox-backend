package de.innovationhub.prox.modules.commons.domain;

import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {

    <S extends T> Optional<S> findById(ID id);

    boolean existsById(ID id);

    long count();
}