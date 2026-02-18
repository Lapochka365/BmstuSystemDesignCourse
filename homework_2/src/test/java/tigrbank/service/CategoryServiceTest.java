package tigrbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tigrbank.domain.Category;
import tigrbank.domain.OperationType;
import tigrbank.repository.inmemory.InMemoryCategoryRepository;
import tigrbank.service.impl.CategoryServiceImpl;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private CategoryServiceImpl service;
    private InMemoryCategoryRepository categoryRepo;

    @BeforeEach
    void setUp() {
        categoryRepo = new InMemoryCategoryRepository();
        service = new CategoryServiceImpl(categoryRepo);
    }

    @Test
    void createCategorySetsFields() {
        Category cat = service.create(OperationType.EXPENSE, "Кафе");
        assertNotNull(cat.getId());
        assertEquals("Кафе", cat.getName());
        assertEquals(OperationType.EXPENSE, cat.getType());
        assertTrue(cat.isActive());
    }

    @Test
    void getByIdReturnsCreated() {
        Category cat = service.create(OperationType.INCOME, "Зарплата");
        assertEquals(cat.getId(), service.getById(cat.getId()).getId());
    }

    @Test
    void getByIdThrowsOnMissing() {
        assertThrows(NoSuchElementException.class,
                () -> service.getById(UUID.randomUUID()));
    }

    @Test
    void getAllReturnsOnlyActive() {
        service.create(OperationType.INCOME, "A");
        Category b = service.create(OperationType.EXPENSE, "B");
        service.delete(b.getId());
        assertEquals(1, service.getAll().size());
    }

    @Test
    void getAllIncludingInactiveReturnsAll() {
        service.create(OperationType.INCOME, "A");
        Category b = service.create(OperationType.EXPENSE, "B");
        service.delete(b.getId());
        assertEquals(2, service.getAllIncludingInactive().size());
    }

    @Test
    void updateNameChangesName() {
        Category cat = service.create(OperationType.EXPENSE, "Старое");
        service.updateName(cat.getId(), "Новое");
        assertEquals("Новое", service.getById(cat.getId()).getName());
    }

    @Test
    void deleteSoftDeletesCategory() {
        Category cat = service.create(OperationType.INCOME, "Удаляемая");
        service.delete(cat.getId());
        assertTrue(service.getAll().isEmpty());
        assertThrows(NoSuchElementException.class,
                () -> service.getById(cat.getId()));
    }
}
