package zoo.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import zoo.animals.Animal;
import zoo.animals.SpeciesType;
import zoo.core.Exhibition;
import zoo.core.Zoo;
import zoo.employees.Employee;
import zoo.employees.EmployeePosition;

public class ReportService {
    public void generateReport(Zoo zoo) {
        System.out.println("=".repeat(10) + " Отчет " + "=".repeat(10));

        List<Employee> employees = zoo.getEmployees();

        System.out.println("\nКол-во сотрудников: " + employees.size());

        Map<EmployeePosition, Long> byEmployeePosition = employees.stream()
                .collect(Collectors.groupingBy(Employee::getPosition, Collectors.counting()));
        System.out.println("По должности:");
        byEmployeePosition.forEach((empPosition, count) -> System.out.println("  " + empPosition + ": " + count));

        List<Animal> animals = zoo.getAllAnimals();

        System.out.println("\nКол-во животных: " + animals.size());

        Map<SpeciesType, Long> bySpecies = animals.stream()
                .collect(Collectors.groupingBy(Animal::getSpeciesType, Collectors.counting()));

        System.out.println("По видам:");
        bySpecies.forEach((speciesType, count) -> System.out.println("  " + speciesType + ": " + count));

        System.out.println("\nРасписание кормления:");
        animals.stream()
                .sorted((a, b) -> Integer.compare(a.getFeedingHour(), b.getFeedingHour()))
                .forEach(a -> System.out
                        .println("  " + String.format("%02d:00", a.getFeedingHour()) + " - " + a.getName()));

        long needAttention = animals.stream()
                .filter(Animal::needsHealing)
                .count();
        System.out.println("\nТребуют лечения: " + needAttention);
        long hungry = animals.stream()
                .filter(Animal::needsFeeding)
                .count();
        System.out.println("Требуют кормления: " + hungry);

        List<Exhibition> exhibitions = zoo.getExhibitions();
        System.out.println("\nВыставки: " + exhibitions.size());
        exhibitions.forEach(
                e -> System.out.println("  " + e.getName() + ": " + e.getExhibitedAnimals().size() + " животных"));

        System.out.println("=".repeat(30));
    }
}
