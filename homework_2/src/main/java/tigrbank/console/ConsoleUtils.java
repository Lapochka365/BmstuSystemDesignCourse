package tigrbank.console;

import tigrbank.domain.BankAccount;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Вспомогательные методы для консольного ввода.
 */
public final class ConsoleUtils {

    private ConsoleUtils() {
    }

    public static UUID readUuid(Scanner scanner, String prompt, List<?> searchPool) {
        System.out.print(prompt + " (UUID): ");
        String input = scanner.nextLine().trim();

        if (input.length() < 36) {
            return findFullUuid(input, searchPool);
        }

        return UUID.fromString(input);
    }

    public static OperationType readOperationType(Scanner scanner) {
        System.out.print("Тип (1 — доход, 2 — расход): ");
        String t = scanner.nextLine().trim();

        if (t.equals("1"))
            return OperationType.INCOME;

        if (t.equals("2"))
            return OperationType.EXPENSE;

        throw new IllegalArgumentException("Неверный тип: введите 1 или 2");
    }

    private static UUID findFullUuid(String prefix, List<?> entities) {
        for (Object entity : entities) {
            UUID id = extractId(entity);
            if (id != null && id.toString().startsWith(prefix)) {
                return id;
            }
        }

        throw new IllegalArgumentException("Сущность с префиксом UUID '" + prefix + "' не найдена");
    }

    private static UUID extractId(Object entity) {
        if (entity instanceof BankAccount a)
            return a.getId();

        if (entity instanceof Category c)
            return c.getId();

        if (entity instanceof Operation op)
            return op.getId();

        return null;
    }
}
