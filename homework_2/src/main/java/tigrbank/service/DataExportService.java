package tigrbank.service;

import tigrbank.domain.BankAccount;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;

import java.io.IOException;
import java.util.List;

public interface DataExportService {

    void exportData(String format, List<BankAccount> accounts,
            List<Category> categories, List<Operation> operations,
            String filePath) throws IOException;

    List<String> supportedFormats();
}
