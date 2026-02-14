package zoo.employees;

import zoo.Cleanable;
import zoo.Feedable;
import zoo.Nameable;

public class Zookeeper extends Employee {
    public Zookeeper(String name) {
        super(name);
    }

    public <T extends Feedable & Nameable> void feed(T animal) {
        if (animal.needsFeeding()) {
            System.out.println(name + " кормит " + animal.getName());
            animal.feed();
        } else {
            System.out.println(animal.getName() + " не нуждается в кормлении");
        }
    }

    public <T extends Cleanable & Nameable> void clean(T cleanable) {
        System.out.println(name + " убирает вольер: " + cleanable.getName());
        cleanable.clean();
    }

    @Override
    public EmployeePosition getPosition() {
        return EmployeePosition.ZOOKEEPER;
    }
}
