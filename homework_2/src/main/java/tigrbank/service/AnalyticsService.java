package tigrbank.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Сервис аналитики: разница доходов/расходов, группировка по категориям.
 */
public interface AnalyticsService {

    /**
     * Разница (доходы − расходы) за период.
     */
    BigDecimal getNetIncome(LocalDate from, LocalDate to);

    /**
     * Группировка расходов по названиям категорий за период.
     */
    Map<String, BigDecimal> getExpensesByCategory(LocalDate from, LocalDate to);

    /**
     * Группировка доходов по названиям категорий за период.
     */
    Map<String, BigDecimal> getIncomeByCategory(LocalDate from, LocalDate to);
}
