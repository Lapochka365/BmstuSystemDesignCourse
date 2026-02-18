package tigrbank.service.impl;

import org.springframework.stereotype.Service;
import tigrbank.domain.BankAccount;
import tigrbank.domain.Operation;
import tigrbank.domain.OperationType;
import tigrbank.repository.BankAccountRepository;
import tigrbank.repository.OperationRepository;
import tigrbank.service.BankAccountService;
import tigrbank.service.Timed;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository accountRepo;
    private final OperationRepository operationRepo;

    public BankAccountServiceImpl(BankAccountRepository accountRepo,
            OperationRepository operationRepo) {
        this.accountRepo = accountRepo;
        this.operationRepo = operationRepo;
    }

    @Override
    @Timed
    public BankAccount create(String name, double initialBalance) {
        BankAccount account = new BankAccount(name, BigDecimal.valueOf(initialBalance));
        accountRepo.add(account);
        return account;
    }

    @Override
    public BankAccount getById(UUID id) {
        return accountRepo.findById(id)
                .filter(BankAccount::isActive)
                .orElseThrow(() -> new NoSuchElementException("Счёт не найден: " + id));
    }

    @Override
    @Timed
    public List<BankAccount> getAll() {
        return accountRepo.findAll().stream()
                .filter(BankAccount::isActive)
                .toList();
    }

    @Override
    public List<BankAccount> getAllIncludingInactive() {
        return accountRepo.findAll();
    }

    @Override
    public void updateName(UUID id, String newName) {
        BankAccount account = getById(id);
        account.setName(newName);
        accountRepo.update(account);
    }

    @Override
    public void delete(UUID id) {
        BankAccount account = getById(id);
        account.setActive(false);
        accountRepo.update(account);
    }

    @Override
    public void recalculateBalance(UUID id) {
        BankAccount account = getById(id);
        List<Operation> ops = operationRepo.findByBankAccountId(id).stream()
                .filter(Operation::isActive)
                .toList();

        BigDecimal balance = BigDecimal.ZERO;
        for (Operation op : ops) {
            if (op.getType() == OperationType.INCOME) {
                balance = balance.add(op.getAmount());
            } else {
                balance = balance.subtract(op.getAmount());
            }
        }
        account.setBalance(balance);
        accountRepo.update(account);
    }
}
