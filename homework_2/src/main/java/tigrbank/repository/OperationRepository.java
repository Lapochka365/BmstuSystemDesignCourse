package tigrbank.repository;

import tigrbank.domain.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с операциями.
 */
public interface OperationRepository extends Repository<Operation> {

    List<Operation> findByBankAccountId(UUID bankAccountId);

    List<Operation> findByDateRange(LocalDate from, LocalDate to);

    List<Operation> findByCategoryId(UUID categoryId);
}
