package zoo.animals;

public class Snake extends Animal {
    public Snake(String name, int health, int hunger, int feedingHour) {
        super(name, health, hunger, feedingHour);
        this.hungerThreshold = 70;
    }

    @Override
    public void feed() {
        System.out.println(name + " ест грызунов");
        this.hunger = Math.max(0, hunger - 50);
    }

    @Override
    public void heal() {
        System.out.println(name + " помещен в карантин");
        this.health = 100;
    }

    @Override
    public SpeciesType getSpeciesType() {
        return SpeciesType.REPTILE;
    }
}
