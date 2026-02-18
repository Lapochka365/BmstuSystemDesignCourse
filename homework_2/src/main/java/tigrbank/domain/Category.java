package tigrbank.domain;

import java.util.UUID;

/**
 * Категория финансовой операции (например, «Кафе», «Зарплата»).
 */
public class Category {

    private final UUID id;
    private OperationType type;
    private String name;
    private boolean active = true;

    public Category(UUID id, OperationType type, String name) {
        if (id == null)
            throw new IllegalArgumentException("ID категории не может быть null");
        if (type == null)
            throw new IllegalArgumentException("Тип категории не может быть null");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Название категории не может быть пустым");
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public Category(OperationType type, String name) {
        this(UUID.randomUUID(), type, name);
    }

    public UUID getId() {
        return id;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        if (type == null)
            throw new IllegalArgumentException("Тип категории не может быть null");
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Название категории не может быть пустым");
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("[ID %s] %s (%s) (Категория активна: %s)", id.toString().substring(0, 8), name, type,
                active ? "да" : "нет");
    }
}
