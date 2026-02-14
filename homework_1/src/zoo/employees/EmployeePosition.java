package zoo.employees;

public enum EmployeePosition {
    ZOOKEEPER("Смотритель"),
    VETERINARIAN("Ветеринар");

    private final String title;

    private EmployeePosition(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
