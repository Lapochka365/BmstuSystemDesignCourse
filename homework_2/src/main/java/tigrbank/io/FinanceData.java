package tigrbank.io;

import tigrbank.domain.BankAccount;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;

import java.util.List;

/**
 * Контейнер для всех финансовых данных (используется при импорте/экспорте).
 */
public record FinanceData(
        List<BankAccount> accounts,
        List<Category> categories,
        List<Operation> operations
) {}
