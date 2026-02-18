package tigrbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tigrbank.domain.BankAccount;
import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;
import tigrbank.repository.inmemory.InMemoryBankAccountRepository;
import tigrbank.repository.inmemory.InMemoryOperationRepository;
import tigrbank.service.impl.BankAccountServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceTest {

    private BankAccountServiceImpl service;
    private InMemoryBankAccountRepository accountRepo;
    private InMemoryOperationRepository operationRepo;

    @BeforeEach
    void setUp() {
        accountRepo = new InMemoryBankAccountRepository();
        operationRepo = new InMemoryOperationRepository();
        service = new BankAccountServiceImpl(accountRepo, operationRepo);
    }

    @Test
    void createAccountSetsNameAndBalance() {
        BankAccount acc = service.create("Тестовый", 1000);
        assertNotNull(acc.getId());
        assertEquals("Тестовый", acc.getName());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(acc.getBalance()));
    }

    @Test
    void getByIdReturnsCreatedAccount() {
        BankAccount acc = service.create("Основной", 500);
        BankAccount found = service.getById(acc.getId());
        assertEquals(acc.getId(), found.getId());
    }

    @Test
    void getByIdThrowsOnMissing() {
        assertThrows(NoSuchElementException.class,
                () -> service.getById(java.util.UUID.randomUUID()));
    }

    @Test
    void getAllReturnsAllAccounts() {
        service.create("A", 0);
        service.create("B", 0);
        assertEquals(2, service.getAll().size());
    }

    @Test
    void updateNameChangesName() {
        BankAccount acc = service.create("Старое", 0);
        service.updateName(acc.getId(), "Новое");
        assertEquals("Новое", service.getById(acc.getId()).getName());
    }

    @Test
    void deleteRemovesAccount() {
        BankAccount acc = service.create("Удаляемый", 0);
        service.delete(acc.getId());
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void recalculateBalanceComputesFromOperations() {
        BankAccount acc = service.create("Тест", 9999);

        operationRepo.add(new Operation(OperationType.INCOME, acc.getId(),
                BigDecimal.valueOf(500), LocalDate.now(), null,
                java.util.UUID.randomUUID()));
        operationRepo.add(new Operation(OperationType.EXPENSE, acc.getId(),
                BigDecimal.valueOf(200), LocalDate.now(), null,
                java.util.UUID.randomUUID()));

        service.recalculateBalance(acc.getId());
        assertEquals(0, BigDecimal.valueOf(300).compareTo(acc.getBalance()));
    }
}
