package tigrbank.console;

import org.springframework.stereotype.Component;
import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;
import tigrbank.service.BankAccountService;
import tigrbank.service.CategoryService;
import tigrbank.service.OperationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class OperationMenuHandler {

    private final OperationService operationService;
    private final BankAccountService accountService;
    private final CategoryService categoryService;

    public OperationMenuHandler(OperationService operationService, BankAccountService accountService,
            CategoryService categoryService) {
        this.operationService = operationService;
        this.accountService = accountService;
        this.categoryService = categoryService;
    }

    public void handle(Scanner scanner) {
        System.out.println("\n-- Операции --");
        System.out.println("1. Список (все)  2. Список (по счёту)  3. Создать  4. Удалить");
        System.out.print("> ");
        String ch = scanner.nextLine().trim();
        switch (ch) {
            case "1" -> {
                List<Operation> all = operationService.getAll();
                if (all.isEmpty())
                    System.out.println("Нет операций.");
                else
                    all.forEach(System.out::println);
            }
            case "2" -> {
                UUID accId = ConsoleUtils.readUuid(scanner, "ID счёта", accountService.getAll());
                operationService.getByBankAccount(accId).forEach(System.out::println);
            }
            case "3" -> {
                OperationType type = ConsoleUtils.readOperationType(scanner);
                UUID accId = ConsoleUtils.readUuid(scanner, "ID счёта", accountService.getAll());
                System.out.print("Сумма: ");
                double amount = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Дата (YYYY-MM-DD): ");
                LocalDate date = LocalDate.parse(scanner.nextLine().trim());
                System.out.print("Описание (Enter — пропустить): ");
                String desc = scanner.nextLine().trim();
                UUID catId = ConsoleUtils.readUuid(scanner, "ID категории", categoryService.getAll());
                Operation op = operationService.create(type, accId, amount, date,
                        desc.isEmpty() ? null : desc, catId);
                System.out.println("Создана: " + op);
            }
            case "4" -> {
                UUID id = ConsoleUtils.readUuid(scanner, "ID операции", operationService.getAll());
                operationService.delete(id);
                System.out.println("Удалено.");
            }
        }
    }
}
