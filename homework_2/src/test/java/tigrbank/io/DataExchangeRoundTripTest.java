package tigrbank.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tigrbank.domain.*;
import tigrbank.io.strategy.CsvStrategy;
import tigrbank.io.strategy.JsonStrategy;
import tigrbank.io.strategy.YamlStrategy;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DataExchangeRoundTripTest {

    private List<BankAccount> accounts;
    private List<Category> categories;
    private List<Operation> operations;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        UUID accId = UUID.randomUUID();
        UUID catIncId = UUID.randomUUID();
        UUID catExpId = UUID.randomUUID();

        accounts = List.of(
                new BankAccount(accId, "Основной счёт", BigDecimal.valueOf(15000))
        );
        categories = List.of(
                new Category(catIncId, OperationType.INCOME, "Зарплата"),
                new Category(catExpId, OperationType.EXPENSE, "Кафе")
        );
        operations = List.of(
                new Operation(UUID.randomUUID(), OperationType.INCOME, accId,
                        BigDecimal.valueOf(50000), LocalDate.of(2025, 1, 10),
                        "Январская зарплата", catIncId),
                new Operation(UUID.randomUUID(), OperationType.EXPENSE, accId,
                        BigDecimal.valueOf(1500), LocalDate.of(2025, 1, 15),
                        "Обед с коллегами", catExpId)
        );
    }

    @Test
    void jsonRoundTrip() throws Exception {
        JsonStrategy strategy = new JsonStrategy();
        String file = tempDir.resolve("data.json").toString();

        strategy.exportData(accounts, categories, operations, file);
        FinanceData imported = strategy.importData(file);

        assertRoundTrip(imported);
    }

    @Test
    void yamlRoundTrip() throws Exception {
        YamlStrategy strategy = new YamlStrategy();
        String file = tempDir.resolve("data.yaml").toString();

        strategy.exportData(accounts, categories, operations, file);
        FinanceData imported = strategy.importData(file);

        assertRoundTrip(imported);
    }

    @Test
    void csvRoundTrip() throws Exception {
        CsvStrategy strategy = new CsvStrategy();
        String file = tempDir.resolve("data.csv").toString();

        strategy.exportData(accounts, categories, operations, file);
        FinanceData imported = strategy.importData(file);

        assertRoundTrip(imported);
    }

    private void assertRoundTrip(FinanceData imported) {
        assertEquals(accounts.size(), imported.accounts().size());
        assertEquals(categories.size(), imported.categories().size());
        assertEquals(operations.size(), imported.operations().size());

        assertEquals(accounts.get(0).getId(), imported.accounts().get(0).getId());
        assertEquals(accounts.get(0).getName(), imported.accounts().get(0).getName());
        assertEquals(0, accounts.get(0).getBalance().compareTo(imported.accounts().get(0).getBalance()));

        assertEquals(categories.get(0).getName(), imported.categories().get(0).getName());
        assertEquals(categories.get(0).getType(), imported.categories().get(0).getType());

        assertEquals(operations.get(0).getAmount().compareTo(imported.operations().get(0).getAmount()), 0);
        assertEquals(operations.get(0).getDate(), imported.operations().get(0).getDate());
        assertEquals(operations.get(0).getDescription(), imported.operations().get(0).getDescription());
    }
}
