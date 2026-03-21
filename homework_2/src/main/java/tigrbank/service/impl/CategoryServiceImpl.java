package tigrbank.service.impl;

import org.springframework.stereotype.Service;
import tigrbank.domain.Category;
import tigrbank.domain.OperationType;
import tigrbank.repository.CategoryRepository;
import tigrbank.service.CategoryService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Category create(OperationType type, String name) {
        Category category = new Category(type, name);
        categoryRepo.add(category);
        return category;
    }

    @Override
    public Category getById(UUID id) {
        return categoryRepo.findById(id)
                .filter(Category::isActive)
                .orElseThrow(() -> new NoSuchElementException("Категория не найдена: " + id));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepo.findAll().stream()
                .filter(Category::isActive)
                .toList();
    }

    @Override
    public List<Category> getAllIncludingInactive() {
        return categoryRepo.findAll();
    }

    @Override
    public void updateName(UUID id, String newName) {
        Category cat = getById(id);
        cat.setName(newName);
        categoryRepo.update(cat);
    }

    @Override
    public void delete(UUID id) {
        Category cat = getById(id);
        cat.setActive(false);
        categoryRepo.update(cat);
    }
}
