package tigrbank.console;

import org.springframework.stereotype.Component;
import tigrbank.service.AnalyticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

@Component
public class AnalyticsMenuHandler {

    private final AnalyticsService analyticsService;

    public AnalyticsMenuHandler(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    public void handle(Scanner scanner) {
        System.out.println("\n-- Аналитика --");
        System.out.print("Дата начала (YYYY-MM-DD): ");
        LocalDate from = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Дата конца  (YYYY-MM-DD): ");
        LocalDate to = LocalDate.parse(scanner.nextLine().trim());

        BigDecimal net = analyticsService.getNetIncome(from, to);
        System.out.printf("Разница (доходы − расходы): %.2f%n", net);

        Map<String, BigDecimal> expenses = analyticsService.getExpensesByCategory(from, to);
        if (!expenses.isEmpty()) {
            System.out.println("Расходы по категориям:");
            expenses.forEach((cat, sum) -> System.out.printf("  %-20s %.2f%n", cat, sum));
        }

        Map<String, BigDecimal> incomes = analyticsService.getIncomeByCategory(from, to);
        if (!incomes.isEmpty()) {
            System.out.println("Доходы по категориям:");
            incomes.forEach((cat, sum) -> System.out.printf("  %-20s %.2f%n", cat, sum));
        }
    }
}
