package tigrbank.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Финансовая операция (доход или расход).
 */
public class Operation {

    private final UUID id;
    private OperationType type;
    private UUID bankAccountId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private UUID categoryId;
    private boolean active = true;

    public Operation(UUID id, OperationType type, UUID bankAccountId,
            BigDecimal amount, LocalDate date, String description, UUID categoryId) {
        if (id == null)
            throw new IllegalArgumentException("ID операции не может быть null");
        if (type == null)
            throw new IllegalArgumentException("Тип операции не может быть null");
        if (bankAccountId == null)
            throw new IllegalArgumentException("ID счёта не может быть null");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма операции должна быть положительной");
        }
        if (date == null)
            throw new IllegalArgumentException("Дата операции не может быть null");
        if (categoryId == null)
            throw new IllegalArgumentException("ID категории не может быть null");
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
    }

    public Operation(OperationType type, UUID bankAccountId,
            BigDecimal amount, LocalDate date, String description, UUID categoryId) {
        this(UUID.randomUUID(), type, bankAccountId, amount, date, description, categoryId);
    }

    public UUID getId() {
        return id;
    }

    public OperationType getType() {
        return type;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setType(OperationType type) {
        if (type == null)
            throw new IllegalArgumentException("Тип не может быть null");
        this.type = type;
    }

    public void setBankAccountId(UUID bankAccountId) {
        if (bankAccountId == null)
            throw new IllegalArgumentException("ID счёта не может быть null");
        this.bankAccountId = bankAccountId;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма операции должна быть положительной");
        }
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Дата не может быть null");
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(UUID categoryId) {
        if (categoryId == null)
            throw new IllegalArgumentException("ID категории не может быть null");
        this.categoryId = categoryId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("[ID %s] %s %.2f (%s) — %s (Операция активна: %s)",
                id.toString().substring(0, 8), type, amount, date,
                description != null ? description : "", active ? "да" : "нет");
    }
}
