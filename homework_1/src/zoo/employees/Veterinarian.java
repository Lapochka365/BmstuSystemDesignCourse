package zoo.employees;

import zoo.Healable;
import zoo.Nameable;

public class Veterinarian extends Employee {
    public Veterinarian(String name) {
        super(name);
    }

    public <T extends Healable & Nameable> void examine(T animal) {
        System.out.println(name + " осматривает " + animal.getName());
        if (animal.needsHealing()) {
            System.out.println("    " + animal.getName() + " требует лечения!");
            animal.heal();
        } else {
            System.out.println("    " + animal.getName() + " здоров!");
        }
    }

    @Override
    public EmployeePosition getPosition() {
        return EmployeePosition.VETERINARIAN;
    }
}
