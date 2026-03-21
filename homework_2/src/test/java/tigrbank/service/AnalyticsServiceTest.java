package tigrbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;
import tigrbank.repository.inmemory.InMemoryCategoryRepository;
import tigrbank.repository.inmemory.InMemoryOperationRepository;
import tigrbank.service.impl.AnalyticsServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsServiceTest {

    private AnalyticsServiceImpl analyticsService;
    private InMemoryOperationRepository operationRepo;
    private InMemoryCategoryRepository categoryRepo;

    private Category salaryCategory;
    private Category cafeCategory;
    private final UUID accountId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        operationRepo = new InMemoryOperationRepository();
        categoryRepo = new InMemoryCategoryRepository();
        analyticsService = new AnalyticsServiceImpl(operationRepo, categoryRepo);

        salaryCategory = new Category(OperationType.INCOME, "Зарплата");
        cafeCategory = new Category(OperationType.EXPENSE, "Кафе");
        categoryRepo.add(salaryCategory);
        categoryRepo.add(cafeCategory);
    }

    @Test
    void netIncomeCalculatesCorrectly() {
        operationRepo.add(new Operation(OperationType.INCOME, accountId,
                BigDecimal.valueOf(5000), LocalDate.of(2025, 1, 15), null, salaryCategory.getId()));
        operationRepo.add(new Operation(OperationType.EXPENSE, accountId,
                BigDecimal.valueOf(1200), LocalDate.of(2025, 1, 20), null, cafeCategory.getId()));

        BigDecimal net = analyticsService.getNetIncome(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));
        assertEquals(0, BigDecimal.valueOf(3800).compareTo(net));
    }

    @Test
    void netIncomeFiltersOutOfRange() {
        operationRepo.add(new Operation(OperationType.INCOME, accountId,
                BigDecimal.valueOf(5000), LocalDate.of(2025, 2, 15), null, salaryCategory.getId()));

        BigDecimal net = analyticsService.getNetIncome(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));
        assertEquals(0, BigDecimal.ZERO.compareTo(net));
    }

    @Test
    void expensesByCategoryGroupsCorrectly() {
        operationRepo.add(new Operation(OperationType.EXPENSE, accountId,
                BigDecimal.valueOf(500), LocalDate.of(2025, 1, 10), null, cafeCategory.getId()));
        operationRepo.add(new Operation(OperationType.EXPENSE, accountId,
                BigDecimal.valueOf(300), LocalDate.of(2025, 1, 20), null, cafeCategory.getId()));

        Map<String, BigDecimal> expenses = analyticsService.getExpensesByCategory(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

        assertEquals(1, expenses.size());
        assertEquals(0, BigDecimal.valueOf(800).compareTo(expenses.get("Кафе")));
    }

    @Test
    void incomeByCategoryGroupsCorrectly() {
        operationRepo.add(new Operation(OperationType.INCOME, accountId,
                BigDecimal.valueOf(50000), LocalDate.of(2025, 1, 5), null, salaryCategory.getId()));

        Map<String, BigDecimal> incomes = analyticsService.getIncomeByCategory(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

        assertEquals(1, incomes.size());
        assertEquals(0, BigDecimal.valueOf(50000).compareTo(incomes.get("Зарплата")));
    }

    @Test
    void netIncomeExcludesInactiveOperations() {
        Operation income = new Operation(OperationType.INCOME, accountId,
                BigDecimal.valueOf(5000), LocalDate.of(2025, 1, 15), null, salaryCategory.getId());
        operationRepo.add(income);

        Operation expense = new Operation(OperationType.EXPENSE, accountId,
                BigDecimal.valueOf(1200), LocalDate.of(2025, 1, 20), null, cafeCategory.getId());
        operationRepo.add(expense);

        expense.setActive(false);
        operationRepo.update(expense);

        BigDecimal net = analyticsService.getNetIncome(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));
        assertEquals(0, BigDecimal.valueOf(5000).compareTo(net));
    }
}
