package tigrbank.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tigrbank.domain.Operation;
import tigrbank.io.DataImportStrategy;
import tigrbank.io.FinanceData;
import tigrbank.repository.BankAccountRepository;
import tigrbank.repository.CategoryRepository;
import tigrbank.repository.OperationRepository;
import tigrbank.service.BankAccountService;
import tigrbank.service.DataImportService;

@Service
public class DataImportServiceImpl implements DataImportService {

    private final Map<String, DataImportStrategy> importers;
    private final BankAccountService accountService;
    private final BankAccountRepository accountRepo;
    private final CategoryRepository categoryRepo;
    private final OperationRepository operationRepo;

    public DataImportServiceImpl(List<DataImportStrategy> importStrategies, BankAccountService accountService,
            BankAccountRepository accountRepo,
            CategoryRepository categoryRepo, OperationRepository operationRepo) {
        this.importers = importStrategies.stream()
                .collect(Collectors.toMap(DataImportStrategy::getFormat, Function.identity()));
        this.accountService = accountService;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
        this.operationRepo = operationRepo;
    }

    @Override
    public FinanceData importAll(String format, String filePath) throws IOException {
        DataImportStrategy strategy = importers.get(format.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Неизвестный формат импорта: " + format
                    + ". Доступные: " + importers.keySet());
        }
        FinanceData data = strategy.importData(filePath);

        data.accounts().stream()
                .filter(a -> accountRepo.findById(a.getId()).isEmpty())
                .forEach(accountRepo::add);
        data.categories().stream()
                .filter(c -> categoryRepo.findById(c.getId()).isEmpty())
                .forEach(categoryRepo::add);
        data.operations().stream()
                .filter(op -> operationRepo.findById(op.getId()).isEmpty())
                .forEach(operationRepo::add);

        data.operations().stream()
                .map(Operation::getBankAccountId)
                .distinct()
                .forEach(accountService::recalculateBalance);

        return data;
    }

    @Override
    public List<String> supportedFormats() {
        return List.copyOf(importers.keySet());
    }
}
