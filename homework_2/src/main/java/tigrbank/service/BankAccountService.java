package tigrbank.service;

import tigrbank.domain.BankAccount;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления банковскими счетами.
 */
public interface BankAccountService {

    BankAccount create(String name, double initialBalance);

    BankAccount getById(UUID id);

    List<BankAccount> getAll();

    List<BankAccount> getAllIncludingInactive();

    void updateName(UUID id, String newName);

    void delete(UUID id);

    /**
     * Пересчёт баланса на основании всех операций по счёту.
     */
    void recalculateBalance(UUID id);
}
