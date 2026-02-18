package tigrbank.repository.inmemory;

import org.springframework.stereotype.Component;
import tigrbank.domain.BankAccount;
import tigrbank.repository.BankAccountRepository;

import java.util.*;

@Component
public class InMemoryBankAccountRepository implements BankAccountRepository {

    private final Map<UUID, BankAccount> storage = new LinkedHashMap<>();

    @Override
    public void add(BankAccount account) {
        storage.put(account.getId(), account);
    }

    @Override
    public Optional<BankAccount> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(BankAccount account) {
        if (!storage.containsKey(account.getId())) {
            throw new NoSuchElementException("Счёт не найден: " + account.getId());
        }
        storage.put(account.getId(), account);
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
