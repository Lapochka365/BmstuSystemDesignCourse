package zoo.animals;

import zoo.Feedable;
import zoo.Healable;
import zoo.Nameable;

public abstract class Animal implements Feedable, Healable, Nameable {
    protected final String name;
    protected int health;
    protected int hunger;
    protected int healthThreshold = 50;
    protected int hungerThreshold = 50;
    protected final int feedingHour;

    public Animal(String name, int health, int hunger, int feedingHour) {
        this.name = name;
        this.health = health;
        this.hunger = hunger;
        this.feedingHour = feedingHour;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getFeedingHour() {
        return feedingHour;
    }

    public abstract SpeciesType getSpeciesType();

    public abstract void feed();

    public abstract void heal();

    @Override
    public boolean needsFeeding() {
        return hunger > hungerThreshold;
    }

    @Override
    public boolean needsHealing() {
        return health < healthThreshold;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "{name='" + name +
                "', health=" + health +
                ", hunger=" + hunger +
                ", healthThreshold=" + healthThreshold +
                ", hungerThreshold=" + hungerThreshold +
                ", feedingHour=" + feedingHour +
                ", speciesType='" + getSpeciesType() + "'}";
    }
}
