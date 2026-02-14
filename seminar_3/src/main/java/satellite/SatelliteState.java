package satellite;

public class SatelliteState {
    private boolean active = false;

    private String statusMessage;

    public SatelliteState() {
        this.statusMessage = "Не активирован";
    }

    public boolean isActive() {
        return this.active;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public boolean activate(boolean hasSufficientPower) {
        if (hasSufficientPower && !this.active) {
            this.active = true;
            this.statusMessage = "Активен";
            return true;
        }

        this.statusMessage = hasSufficientPower ? "Уже активен" : "Недостаточно энергии";

        return false;
    }

    public void deactivate() {
        this.active = false;
        this.statusMessage = "Деактивирован";
    }

    @Override
    public String toString() {
        return "SatelliteState{" + "active=" + this.active + ", statusMessage='" + this.statusMessage + "'}";
    }
}
