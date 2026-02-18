package tigrbank.console;

import org.springframework.stereotype.Component;
import tigrbank.domain.BankAccount;
import tigrbank.service.BankAccountService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class AccountMenuHandler {

    private final BankAccountService accountService;

    public AccountMenuHandler(BankAccountService accountService) {
        this.accountService = accountService;
    }

    public void handle(Scanner scanner) {
        System.out.println("\n-- Счета --");
        System.out.println("1. Список  2. Создать  3. Переименовать  4. Удалить");
        System.out.print("> ");
        String ch = scanner.nextLine().trim();
        switch (ch) {
            case "1" -> {
                List<BankAccount> all = accountService.getAll();
                if (all.isEmpty())
                    System.out.println("Нет счетов.");
                else
                    all.forEach(System.out::println);
            }
            case "2" -> {
                System.out.print("Название: ");
                String name = scanner.nextLine().trim();
                System.out.print("Начальный баланс: ");
                double balance = Double.parseDouble(scanner.nextLine().trim());
                BankAccount acc = accountService.create(name, balance);
                System.out.println("Создан: " + acc);
            }
            case "3" -> {
                UUID id = ConsoleUtils.readUuid(scanner, "ID счёта", accountService.getAll());
                System.out.print("Новое название: ");
                String newName = scanner.nextLine().trim();
                accountService.updateName(id, newName);
                System.out.println("Обновлено.");
            }
            case "4" -> {
                UUID id = ConsoleUtils.readUuid(scanner, "ID счёта", accountService.getAll());
                accountService.delete(id);
                System.out.println("Удалено.");
            }
        }
    }
}
