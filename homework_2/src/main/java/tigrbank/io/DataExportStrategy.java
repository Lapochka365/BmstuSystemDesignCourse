package tigrbank.io;

import tigrbank.domain.BankAccount;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;

import java.io.IOException;
import java.util.List;

/**
 * Стратегия экспорта данных.
 */
public interface DataExportStrategy {

    /**
     * Формат файла, который поддерживает данная стратегия (например, "json", "csv").
     */
    String getFormat();

    void exportData(List<BankAccount> accounts, List<Category> categories,
                    List<Operation> operations, String filePath) throws IOException;
}
