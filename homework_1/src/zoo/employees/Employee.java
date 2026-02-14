package zoo.employees;

public abstract class Employee {
    protected final String name;

    public Employee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract EmployeePosition getPosition();
}
