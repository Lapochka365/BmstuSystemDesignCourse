package zoo.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import zoo.Nameable;
import zoo.animals.Animal;

public class Exhibition implements Nameable {
    private final String name;
    private final List<Enclosure> enclosures = new ArrayList<>();

    public Exhibition(String name) {
        this.name = name;
    }

    public void addEnclosure(Enclosure enclosure) {
        enclosures.add(enclosure);
    }

    public List<Enclosure> getEnclosures() {
        return enclosures;
    }

    public List<Animal> getExhibitedAnimals() {
        return enclosures.stream()
                .flatMap(e -> e.getAnimals().stream())
                .toList();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Выставка{'" + name + "', вольеры: "
                + enclosures.stream().map((enc) -> enc.getName()).collect(Collectors.joining(", ")) + "}";
    }
}
