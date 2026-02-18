package tigrbank.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {

    private final AccountMenuHandler accountHandler;
    private final CategoryMenuHandler categoryHandler;
    private final OperationMenuHandler operationHandler;
    private final AnalyticsMenuHandler analyticsHandler;
    private final DataMenuHandler dataHandler;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleApp(AccountMenuHandler accountHandler,
            CategoryMenuHandler categoryHandler,
            OperationMenuHandler operationHandler,
            AnalyticsMenuHandler analyticsHandler,
            DataMenuHandler dataHandler) {
        this.accountHandler = accountHandler;
        this.categoryHandler = categoryHandler;
        this.operationHandler = operationHandler;
        this.analyticsHandler = analyticsHandler;
        this.dataHandler = dataHandler;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== ТигрБанк — Модуль «Учёт финансов» ===");
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> accountHandler.handle(scanner);
                    case "2" -> categoryHandler.handle(scanner);
                    case "3" -> operationHandler.handle(scanner);
                    case "4" -> analyticsHandler.handle(scanner);
                    case "5" -> dataHandler.handleExport(scanner);
                    case "6" -> dataHandler.handleImport(scanner);
                    case "7" -> dataHandler.handleRecalculate(scanner);
                    case "0" -> running = false;
                    default -> System.out.println("Неизвестная команда.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка ввода: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("Не найдено: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Ошибка файла: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Непредвиденная ошибка: " + e.getMessage());
            }
        }
        System.out.println("До свидания!");
    }

    private void printMainMenu() {
        System.out.println("\n--- Главное меню ---");
        System.out.println("1. Счета");
        System.out.println("2. Категории");
        System.out.println("3. Операции");
        System.out.println("4. Аналитика");
        System.out.println("5. Экспорт данных");
        System.out.println("6. Импорт данных");
        System.out.println("7. Пересчёт баланса");
        System.out.println("0. Выход");
        System.out.print("> ");
    }
}
