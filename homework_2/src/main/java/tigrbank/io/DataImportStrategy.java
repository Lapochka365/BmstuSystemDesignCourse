package tigrbank.io;

import java.io.IOException;

/**
 * Стратегия импорта данных.
 */
public interface DataImportStrategy {

    /**
     * Формат файла, который поддерживает данная стратегия (например, "json", "csv").
     */
    String getFormat();

    FinanceData importData(String filePath) throws IOException;
}
