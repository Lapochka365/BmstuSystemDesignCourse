package zoo.core;

import java.util.ArrayList;
import java.util.List;

import zoo.Cleanable;
import zoo.Nameable;
import zoo.animals.Animal;

public class Enclosure implements Cleanable, Nameable {
    private final String name;
    private List<Animal> animals = new ArrayList<>();

    public Enclosure(String name) {
        this.name = name;
    }

    void addAnimal(Animal animal) {
        animals.add(animal);
        System.out.println(animal.getName() + " размещён в вольере: " + name);
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public String getName() {
        return name;
    }

    @Override
    public void clean() {
        System.out.println("Вольер " + name + " убран");
    }
}
