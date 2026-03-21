package tigrbank.repository.inmemory;

import org.springframework.stereotype.Component;
import tigrbank.domain.Category;
import tigrbank.repository.CategoryRepository;

import java.util.*;

@Component
public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<UUID, Category> storage = new LinkedHashMap<>();

    @Override
    public void add(Category category) {
        storage.put(category.getId(), category);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Category category) {
        if (!storage.containsKey(category.getId())) {
            throw new NoSuchElementException("Категория не найдена: " + category.getId());
        }
        storage.put(category.getId(), category);
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
