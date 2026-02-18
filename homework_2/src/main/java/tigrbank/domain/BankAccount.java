package tigrbank.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Банковский счёт пользователя.
 */
public class BankAccount {

    private final UUID id;
    private String name;
    private BigDecimal balance;
    private boolean active = true;

    public BankAccount(UUID id, String name, BigDecimal balance) {
        if (id == null)
            throw new IllegalArgumentException("ID счёта не может быть null");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Название счёта не может быть пустым");
        if (balance == null)
            throw new IllegalArgumentException("Баланс не может быть null");
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public BankAccount(String name, BigDecimal balance) {
        this(UUID.randomUUID(), name, balance);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Название счёта не может быть пустым");
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if (balance == null)
            throw new IllegalArgumentException("Баланс не может быть null");
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("[ID %s] %s — баланс: %.2f (Счёт активен: %s)", id.toString().substring(0, 8), name,
                balance, active ? "да" : "нет");
    }
}
