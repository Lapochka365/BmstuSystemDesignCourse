package zoo.animals;

public class Lion extends Animal {
    public Lion(String name, int health, int hunger, int feedingHour) {
        super(name, health, hunger, feedingHour);
    }

    @Override
    public void feed() {
        System.out.println(name + " ест много сырого мяса");
        this.hunger = Math.max(0, hunger - 40);
    }

    @Override
    public void heal() {
        System.out.println(name + " усыплен и получает лечение");
        this.health = 100;
    }

    @Override
    public SpeciesType getSpeciesType() {
        return SpeciesType.MAMMAL;
    }
}
