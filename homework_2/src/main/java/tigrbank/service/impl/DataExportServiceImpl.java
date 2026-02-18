package tigrbank.service.impl;

import org.springframework.stereotype.Service;
import tigrbank.domain.BankAccount;
import tigrbank.domain.Category;
import tigrbank.domain.Operation;
import tigrbank.io.DataExportStrategy;
import tigrbank.service.DataExportService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataExportServiceImpl implements DataExportService {

    private final Map<String, DataExportStrategy> exporters;

    public DataExportServiceImpl(List<DataExportStrategy> exportStrategies) {
        this.exporters = exportStrategies.stream()
                .collect(Collectors.toMap(DataExportStrategy::getFormat, Function.identity()));
    }

    @Override
    public void exportData(String format, List<BankAccount> accounts,
            List<Category> categories, List<Operation> operations,
            String filePath) throws IOException {
        DataExportStrategy strategy = exporters.get(format.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Неизвестный формат экспорта: " + format
                    + ". Доступные: " + exporters.keySet());
        }
        strategy.exportData(accounts, categories, operations, filePath);
    }

    @Override
    public List<String> supportedFormats() {
        return List.copyOf(exporters.keySet());
    }
}
