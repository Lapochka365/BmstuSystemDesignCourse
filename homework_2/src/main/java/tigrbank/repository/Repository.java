package tigrbank.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Обобщённый репозиторий для CRUD-операций.
 *
 * @param <T> тип доменной сущности
 */
public interface Repository<T> {

    void add(T entity);

    Optional<T> findById(UUID id);

    List<T> findAll();

    void update(T entity);

    void deleteById(UUID id);
}
