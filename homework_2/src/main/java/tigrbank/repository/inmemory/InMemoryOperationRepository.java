package tigrbank.repository.inmemory;

import org.springframework.stereotype.Component;
import tigrbank.domain.Operation;
import tigrbank.repository.OperationRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryOperationRepository implements OperationRepository {

    private final Map<UUID, Operation> storage = new LinkedHashMap<>();

    @Override
    public void add(Operation operation) {
        storage.put(operation.getId(), operation);
    }

    @Override
    public Optional<Operation> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Operation> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Operation operation) {
        if (!storage.containsKey(operation.getId())) {
            throw new NoSuchElementException("Операция не найдена: " + operation.getId());
        }
        storage.put(operation.getId(), operation);
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

    @Override
    public List<Operation> findByBankAccountId(UUID bankAccountId) {
        return storage.values().stream()
                .filter(op -> op.getBankAccountId().equals(bankAccountId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByDateRange(LocalDate from, LocalDate to) {
        return storage.values().stream()
                .filter(op -> !op.getDate().isBefore(from) && !op.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByCategoryId(UUID categoryId) {
        return storage.values().stream()
                .filter(op -> op.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }
}
