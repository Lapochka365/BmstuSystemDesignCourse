public class SatelliteState {
    private boolean active;

    public SatelliteState() {
        this.active = false;
    }

    public boolean isActive() {
        return this.active;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
