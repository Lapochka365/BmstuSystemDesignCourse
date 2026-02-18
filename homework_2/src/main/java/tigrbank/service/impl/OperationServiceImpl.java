package tigrbank.service.impl;

import org.springframework.stereotype.Service;
import tigrbank.domain.BankAccount;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;
import tigrbank.repository.BankAccountRepository;
import tigrbank.repository.CategoryRepository;
import tigrbank.repository.OperationRepository;
import tigrbank.service.OperationService;
import tigrbank.service.Timed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepo;
    private final BankAccountRepository accountRepo;
    private final CategoryRepository categoryRepo;

    public OperationServiceImpl(OperationRepository operationRepo,
            BankAccountRepository accountRepo, CategoryRepository categoryRepo) {
        this.operationRepo = operationRepo;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    @Timed
    public Operation create(OperationType type, UUID bankAccountId,
            double amount, LocalDate date, String description, UUID categoryId) {
        BankAccount account = accountRepo.findById(bankAccountId)
                .filter(BankAccount::isActive)
                .orElseThrow(() -> new NoSuchElementException("Счёт не найден: " + bankAccountId));

        Category category = categoryRepo.findById(categoryId)
                .filter(Category::isActive)
                .orElseThrow(() -> new NoSuchElementException("Категория не найдена: " + categoryId));

        if (category.getType() != type) {
            throw new IllegalArgumentException(
                    "Тип операции (" + type + ") не совпадает с типом категории (" + category.getType() + ")");
        }

        Operation operation = new Operation(type, bankAccountId,
                BigDecimal.valueOf(amount), date, description, categoryId);
        operationRepo.add(operation);

        // Обновляем баланс счёта
        applyToBalance(account, type, BigDecimal.valueOf(amount));
        accountRepo.update(account);

        return operation;
    }

    @Override
    public Operation getById(UUID id) {
        return operationRepo.findById(id)
                .filter(Operation::isActive)
                .orElseThrow(() -> new NoSuchElementException("Операция не найдена: " + id));
    }

    @Override
    public List<Operation> getAll() {
        return operationRepo.findAll().stream()
                .filter(Operation::isActive)
                .toList();
    }

    @Override
    public List<Operation> getAllIncludingInactive() {
        return operationRepo.findAll();
    }

    @Override
    public List<Operation> getByBankAccount(UUID bankAccountId) {
        return operationRepo.findByBankAccountId(bankAccountId).stream()
                .filter(Operation::isActive)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        Operation op = getById(id);
        BankAccount account = accountRepo.findById(op.getBankAccountId()).orElse(null);

        op.setActive(false);
        operationRepo.update(op);

        // Откатываем баланс
        if (account != null) {
            OperationType reverseType = op.getType() == OperationType.INCOME
                    ? OperationType.EXPENSE
                    : OperationType.INCOME;
            applyToBalance(account, reverseType, op.getAmount());
            accountRepo.update(account);
        }
    }

    private void applyToBalance(BankAccount account, OperationType type, BigDecimal amount) {
        if (type == OperationType.INCOME) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }
    }
}
