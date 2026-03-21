package tigrbank.service;

import org.junit.jupiter.api.Test;
import tigrbank.domain.BankAccount;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DomainValidationTest {

    @Test
    void bankAccountRejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("", BigDecimal.ZERO));
    }

    @Test
    void bankAccountRejectsNullBalance() {
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("Test", null));
    }

    @Test
    void categoryRejectsNullType() {
        assertThrows(IllegalArgumentException.class,
                () -> new Category(null, "Test"));
    }

    @Test
    void operationRejectsZeroAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Operation(OperationType.INCOME, UUID.randomUUID(),
                        BigDecimal.ZERO, LocalDate.now(), null, UUID.randomUUID()));
    }

    @Test
    void operationRejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Operation(OperationType.EXPENSE, UUID.randomUUID(),
                        BigDecimal.valueOf(-100), LocalDate.now(), null, UUID.randomUUID()));
    }

    @Test
    void operationAllowsNullDescription() {
        assertDoesNotThrow(() -> new Operation(OperationType.INCOME, UUID.randomUUID(),
                BigDecimal.ONE, LocalDate.now(), null, UUID.randomUUID()));
    }
}
