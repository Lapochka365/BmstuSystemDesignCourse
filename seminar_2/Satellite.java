public abstract class Satellite {
    protected String name;
    protected SatelliteState state;
    protected EnergySystem energy;

    Satellite(String name, double batteryLevel) {
        this.name = name;
        this.state = new SatelliteState();
        this.energy = new EnergySystem(batteryLevel);
        System.out.println("Создан спутник: " + this.name + " (заряд: " + this.energy.getBatteryLevel() * 100 + "%)");
    }

    public boolean activate() {
        double batteryLevel = this.energy.getBatteryLevel();
        if (batteryLevel > 0.2) {
            this.state.activate();
            System.out.println(this.name + ": Активация успешна");
            return true;
        } else {
            System.out.println(this.name + ": Ошибка активации (заряд: " + batteryLevel * 100 + "%)");
            return false;
        }
    }

    public void deactivate() {
        if (this.state.isActive()) {
            this.state.deactivate();
            System.out.println(this.name + ": Деактивация успешна");
        } else {
            System.out.println(this.name + ": Ошибка деактивации (спутник не активирован)");
        }
    }

    public void consumeBattery(double amount) {
        this.energy.consume(amount);
        double batteryLevel = this.energy.getBatteryLevel();
        if (batteryLevel < 0.2) {
            System.out.println(this.name + ": Начата деактивация, критически мало заряда (заряд: "
                    + batteryLevel * 100 + "%)");
            this.deactivate();
        }
    }

    protected abstract void performMission();
}
