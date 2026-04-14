public class EnergySystem {
    private double batteryLevel;

    private static final double LOW_BATTERY_THRESHOLD = 0.2;
    private static final double MAX_BATTERY = 1.0;
    private static final double MIN_BATTERY = 0.0;

    public EnergySystem(double batteryLevel) {
        this.batteryLevel = Math.max(MIN_BATTERY, Math.min(MAX_BATTERY, batteryLevel));
    }

    public double getBatteryLevel() {
        return this.batteryLevel;
    }

    public boolean consume(double amount) {
        if (amount <= 0 || this.batteryLevel <= MIN_BATTERY) {
            return false;
        }

        this.batteryLevel = Math.max(MIN_BATTERY, this.batteryLevel - amount);

        return true;
    }

    public boolean hasSufficientPower() {
        return this.batteryLevel > LOW_BATTERY_THRESHOLD;
    }

    @Override
    public String toString() {
        return "EnergySystem={" + "batteryLevel=" + this.batteryLevel + "}";
    }
}
