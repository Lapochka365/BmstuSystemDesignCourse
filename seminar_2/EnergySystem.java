public class EnergySystem {
    private double batteryLevel;

    public EnergySystem(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public double getBatteryLevel() {
        return this.batteryLevel;
    }

    public void consume(double amount) {
        this.batteryLevel -= amount;
    }
}
