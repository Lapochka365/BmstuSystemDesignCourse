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

    public SatelliteState getState() {
        return this.state;
    }

    public EnergySystem getEnergy() {
        return this.energy;
    }

    public boolean activate() {
        if (this.state.activate(this.energy.hasSufficientPower())) {
            System.out.println(this.name + ": Активация успешна");
            return true;
        } else {
            System.out.println(this.name + ": Ошибка активации (заряд: " + this.energy.getBatteryLevel() * 100 + "%)");
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
        if (!this.energy.hasSufficientPower()) {
            System.out.println(this.name + ": Начата деактивация, критически мало заряда (заряд: "
                    + this.energy.getBatteryLevel() * 100 + "%)");
            this.deactivate();
        }
    }

    protected abstract void performMission();
}
