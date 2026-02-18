package tigrbank.console;

import org.springframework.stereotype.Component;
import tigrbank.domain.Category;
import tigrbank.domain.OperationType;
import tigrbank.service.CategoryService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class CategoryMenuHandler {

    private final CategoryService categoryService;

    public CategoryMenuHandler(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void handle(Scanner scanner) {
        System.out.println("\n-- Категории --");
        System.out.println("1. Список  2. Создать  3. Переименовать  4. Удалить");
        System.out.print("> ");
        String ch = scanner.nextLine().trim();
        switch (ch) {
            case "1" -> {
                List<Category> all = categoryService.getAll();
                if (all.isEmpty())
                    System.out.println("Нет категорий.");
                else
                    all.forEach(System.out::println);
            }
            case "2" -> {
                OperationType type = ConsoleUtils.readOperationType(scanner);
                System.out.print("Название: ");
                String name = scanner.nextLine().trim();
                Category cat = categoryService.create(type, name);
                System.out.println("Создана: " + cat);
            }
            case "3" -> {
                UUID id = ConsoleUtils.readUuid(scanner, "ID категории", categoryService.getAll());
                System.out.print("Новое название: ");
                String newName = scanner.nextLine().trim();
                categoryService.updateName(id, newName);
                System.out.println("Обновлено.");
            }
            case "4" -> {
                UUID id = ConsoleUtils.readUuid(scanner, "ID категории", categoryService.getAll());
                categoryService.delete(id);
                System.out.println("Удалено.");
            }
        }
    }
}
