public abstract class Satellite {
    protected String name;
    protected boolean isActive;
    protected double batteryLevel;

    Satellite(String name, double batteryLevel) {
        this.name = name;
        this.batteryLevel = batteryLevel;
        System.out.println("Создан спутник: " + this.name + " (заряд: " + this.batteryLevel * 100 + "%)");
    }

    public boolean activate() {
        if (this.batteryLevel > 0.2) {
            this.isActive = true;
            System.out.println(this.name + ": Активация успешна");
            return true;
        } else {
            System.out.println(this.name + ": Ошибка активации (заряд: " + this.batteryLevel * 100 + "%)");
            return false;
        }
    }

    public void deactivate() {
        if (this.isActive) {
            this.isActive = false;
            System.out.println(this.name + ": Деактивация успешна");
        } else {
            System.out.println(this.name + ": Ошибка деактивации (спутник не активирован)");
        }
    }

    public void consumeBattery(double amount) {
        this.batteryLevel -= amount;
        if ((this.batteryLevel) < 0.2) {
            System.out.println(this.name + ": Начата деактивация, критически мало заряда (заряд: "
                    + this.batteryLevel * 100 + "%)");
            this.deactivate();
        }
    }

    protected abstract void performMission();
}
