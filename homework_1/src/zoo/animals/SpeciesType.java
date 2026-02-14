package zoo.animals;

public enum SpeciesType {
    MAMMAL("Млекопитающие"),
    BIRD("Птицы"),
    REPTILE("Пресмыкающиеся");

    private final String speciesName;

    private SpeciesType(String name) {
        this.speciesName = name;
    }

    public String getName() {
        return speciesName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
