package tigrbank.service;

import tigrbank.domain.Category;
import tigrbank.domain.OperationType;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления категориями операций.
 */
public interface CategoryService {

    Category create(OperationType type, String name);

    Category getById(UUID id);

    List<Category> getAll();

    List<Category> getAllIncludingInactive();

    void updateName(UUID id, String newName);

    void delete(UUID id);
}
