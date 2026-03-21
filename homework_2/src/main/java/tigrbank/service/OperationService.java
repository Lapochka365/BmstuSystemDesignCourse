package tigrbank.service;

import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления финансовыми операциями.
 */
public interface OperationService {

    Operation create(OperationType type, UUID bankAccountId,
            double amount, LocalDate date, String description, UUID categoryId);

    Operation getById(UUID id);

    List<Operation> getAll();

    List<Operation> getAllIncludingInactive();

    List<Operation> getByBankAccount(UUID bankAccountId);

    void delete(UUID id);
}
