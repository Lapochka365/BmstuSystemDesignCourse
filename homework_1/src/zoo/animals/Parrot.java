package zoo.animals;

public class Parrot extends Animal {
    public Parrot(String name, int health, int hunger, int feedingHour) {
        super(name, health, hunger, feedingHour);
    }

    @Override
    public void feed() {
        System.out.println(name + " ест зерна и фрукты");
        this.hunger = Math.max(0, hunger - 30);
    }

    @Override
    public void heal() {
        System.out.println(name + " получает лекарство в воду");
        this.health = 100;
    }

    @Override
    public SpeciesType getSpeciesType() {
        return SpeciesType.BIRD;
    }
}
