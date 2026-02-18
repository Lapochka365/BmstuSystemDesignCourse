package tigrbank.service;

import java.io.IOException;
import java.util.List;

import tigrbank.io.FinanceData;

public interface DataImportService {
    FinanceData importAll(String format, String filePath) throws IOException;

    List<String> supportedFormats();
}
