package tigrbank.console;

import org.springframework.stereotype.Component;
import tigrbank.domain.BankAccount;
import tigrbank.io.FinanceData;
import tigrbank.service.BankAccountService;
import tigrbank.service.CategoryService;
import tigrbank.service.DataExportService;
import tigrbank.service.DataImportService;
import tigrbank.service.OperationService;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

@Component
public class DataMenuHandler {

    private final DataExportService dataExportService;
    private final DataImportService dataImportService;
    private final BankAccountService accountService;
    private final CategoryService categoryService;
    private final OperationService operationService;

    public DataMenuHandler(DataExportService dataExportService,
            DataImportService dataImportService,
            BankAccountService accountService,
            CategoryService categoryService,
            OperationService operationService) {
        this.dataExportService = dataExportService;
        this.dataImportService = dataImportService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.operationService = operationService;
    }

    public void handleExport(Scanner scanner) throws IOException {
        System.out.print("Формат (" + dataExportService.supportedFormats() + "): ");
        String format = scanner.nextLine().trim();
        System.out.print("Путь к файлу: ");
        String path = scanner.nextLine().trim();
        if (path.isEmpty()) {
            System.out.println("Ошибка: путь к файлу не может быть пустым.");
            return;
        }
        dataExportService.exportData(format,
                accountService.getAllIncludingInactive(),
                categoryService.getAllIncludingInactive(),
                operationService.getAllIncludingInactive(),
                path);
        System.out.println("Данные экспортированы в " + path);
    }

    public void handleImport(Scanner scanner) throws IOException {
        System.out.print("Формат (" + dataImportService.supportedFormats() + "): ");
        String format = scanner.nextLine().trim();
        System.out.print("Путь к файлу: ");
        String path = scanner.nextLine().trim();
        if (path.isEmpty()) {
            System.out.println("Ошибка: путь к файлу не может быть пустым.");
            return;
        }
        FinanceData data = dataImportService.importAll(format, path);
        System.out.printf("Импортировано: %d счетов, %d категорий, %d операций%n",
                data.accounts().size(), data.categories().size(), data.operations().size());
    }

    public void handleRecalculate(Scanner scanner) {
        UUID accId = ConsoleUtils.readUuid(scanner, "ID счёта для пересчёта", accountService.getAll());
        accountService.recalculateBalance(accId);
        BankAccount acc = accountService.getById(accId);
        System.out.println("Баланс после пересчёта: " + acc);
    }
}
