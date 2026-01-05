package zoo.core;

import java.util.ArrayList;
import java.util.List;

import zoo.animals.Animal;
import zoo.employees.Employee;
import zoo.employees.Veterinarian;

public class Zoo {
    private List<Enclosure> enclosures = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();
    private List<Exhibition> exhibitions = new ArrayList<>();

    public void addEnclosure(Enclosure enc) {
        enclosures.add(enc);
        System.out.println("В зоопарке новый вольер: " + enc.getName());
    }

    public void addEmployee(Employee emp) {
        employees.add(emp);
        System.out.println("Сотрудник принят: " + emp.getName());
    }

    public void addAnimal(Animal animal, Enclosure enclosure, Veterinarian vet) {
        System.out.println("Приём животного: " + animal.getName());
        vet.examine(animal);
        enclosure.addAnimal(animal);
    }

    public void addExhibition(Exhibition exhibition) {
        exhibitions.add(exhibition);
        System.out.println("Новая выставка: " + exhibition.getName());
    }

    public List<Exhibition> getExhibitions() {
        return exhibitions;
    }

    public List<Enclosure> getEnclosures() {
        return enclosures;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Animal> getAllAnimals() {
        List<Animal> all = new ArrayList<>();
        for (Enclosure e : enclosures) {
            all.addAll(e.getAnimals());
        }
        return all;
    }

    public int getTotalAnimalCount() {
        return getAllAnimals().size();
    }
}
