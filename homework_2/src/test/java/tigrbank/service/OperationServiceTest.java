package tigrbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tigrbank.domain.BankAccount;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;
import tigrbank.repository.inmemory.InMemoryBankAccountRepository;
import tigrbank.repository.inmemory.InMemoryCategoryRepository;
import tigrbank.repository.inmemory.InMemoryOperationRepository;
import tigrbank.service.impl.OperationServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OperationServiceTest {

    private OperationServiceImpl service;
    private InMemoryBankAccountRepository accountRepo;
    private InMemoryOperationRepository operationRepo;
    private InMemoryCategoryRepository categoryRepo;

    private BankAccount testAccount;
    private Category incomeCategory;
    private Category expenseCategory;

    @BeforeEach
    void setUp() {
        accountRepo = new InMemoryBankAccountRepository();
        operationRepo = new InMemoryOperationRepository();
        categoryRepo = new InMemoryCategoryRepository();
        service = new OperationServiceImpl(operationRepo, accountRepo, categoryRepo);

        testAccount = new BankAccount("Тестовый", BigDecimal.ZERO);
        accountRepo.add(testAccount);

        incomeCategory = new Category(OperationType.INCOME, "Зарплата");
        expenseCategory = new Category(OperationType.EXPENSE, "Кафе");
        categoryRepo.add(incomeCategory);
        categoryRepo.add(expenseCategory);
    }

    @Test
    void createIncomeIncreasesBalance() {
        service.create(OperationType.INCOME, testAccount.getId(),
                500, LocalDate.now(), "Зарплата", incomeCategory.getId());
        assertEquals(0, BigDecimal.valueOf(500).compareTo(testAccount.getBalance()));
    }

    @Test
    void createExpenseDecreasesBalance() {
        testAccount.setBalance(BigDecimal.valueOf(1000));
        accountRepo.update(testAccount);

        service.create(OperationType.EXPENSE, testAccount.getId(),
                300, LocalDate.now(), "Кафе", expenseCategory.getId());
        assertEquals(0, BigDecimal.valueOf(700).compareTo(testAccount.getBalance()));
    }

    @Test
    void deleteOperationRevertsBalance() {
        Operation op = service.create(OperationType.INCOME, testAccount.getId(),
                1000, LocalDate.now(), null, incomeCategory.getId());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(testAccount.getBalance()));

        service.delete(op.getId());
        assertEquals(0, BigDecimal.ZERO.compareTo(testAccount.getBalance()));
    }

    @Test
    void getByBankAccountFiltersCorrectly() {
        UUID otherId = UUID.randomUUID();
        BankAccount other = new BankAccount(otherId, "Другой", BigDecimal.ZERO);
        accountRepo.add(other);

        service.create(OperationType.INCOME, testAccount.getId(),
                100, LocalDate.now(), null, incomeCategory.getId());
        service.create(OperationType.INCOME, otherId,
                200, LocalDate.now(), null, incomeCategory.getId());

        assertEquals(1, service.getByBankAccount(testAccount.getId()).size());
        assertEquals(1, service.getByBankAccount(otherId).size());
    }

    @Test
    void createWithNegativeAmountThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.create(OperationType.INCOME, testAccount.getId(),
                -100, LocalDate.now(), null, incomeCategory.getId()));
    }

    @Test
    void createWithMismatchedCategoryTypeThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.create(OperationType.INCOME, testAccount.getId(),
                100, LocalDate.now(), null, expenseCategory.getId()));
    }

    @Test
    void createWithNonExistentAccountThrows() {
        assertThrows(NoSuchElementException.class, () -> service.create(OperationType.INCOME, UUID.randomUUID(),
                100, LocalDate.now(), null, incomeCategory.getId()));
    }

    @Test
    void createWithNonExistentCategoryThrows() {
        assertThrows(NoSuchElementException.class, () -> service.create(OperationType.INCOME, testAccount.getId(),
                100, LocalDate.now(), null, UUID.randomUUID()));
    }

    @Test
    void deletedOperationNotReturnedByGetAll() {
        Operation op = service.create(OperationType.INCOME, testAccount.getId(),
                100, LocalDate.now(), null, incomeCategory.getId());
        service.delete(op.getId());
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void deletedOperationStillInAllIncludingInactive() {
        Operation op = service.create(OperationType.INCOME, testAccount.getId(),
                100, LocalDate.now(), null, incomeCategory.getId());
        service.delete(op.getId());
        assertEquals(1, service.getAllIncludingInactive().size());
    }
}
