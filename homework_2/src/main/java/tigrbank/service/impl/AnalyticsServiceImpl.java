package tigrbank.service.impl;

import org.springframework.stereotype.Service;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;
import tigrbank.repository.CategoryRepository;
import tigrbank.repository.OperationRepository;
import tigrbank.service.AnalyticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OperationRepository operationRepo;
    private final CategoryRepository categoryRepo;

    public AnalyticsServiceImpl(OperationRepository operationRepo,
            CategoryRepository categoryRepo) {
        this.operationRepo = operationRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public BigDecimal getNetIncome(LocalDate from, LocalDate to) {
        List<Operation> ops = operationRepo.findByDateRange(from, to).stream().filter(Operation::isActive).toList();
        BigDecimal income = sumByType(ops, OperationType.INCOME);
        BigDecimal expense = sumByType(ops, OperationType.EXPENSE);
        return income.subtract(expense);
    }

    @Override
    public Map<String, BigDecimal> getExpensesByCategory(LocalDate from, LocalDate to) {
        return groupByCategory(operationRepo.findByDateRange(from, to).stream().filter(Operation::isActive).toList(),
                OperationType.EXPENSE);
    }

    @Override
    public Map<String, BigDecimal> getIncomeByCategory(LocalDate from, LocalDate to) {
        return groupByCategory(operationRepo.findByDateRange(from, to).stream().filter(Operation::isActive).toList(),
                OperationType.INCOME);
    }

    private BigDecimal sumByType(List<Operation> operations, OperationType type) {
        return operations.stream()
                .filter(op -> op.getType() == type)
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, BigDecimal> groupByCategory(List<Operation> operations, OperationType type) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (Operation op : operations) {
            if (op.getType() != type)
                continue;
            String categoryName = categoryRepo.findById(op.getCategoryId())
                    .map(Category::getName)
                    .orElse("Неизвестная категория");
            result.merge(categoryName, op.getAmount(), BigDecimal::add);
        }
        return result;
    }
}
