public class ImagingSatellite extends Satellite {
    private double resolution;
    private int photosTaken;

    ImagingSatellite(String name, double batteryLevel, double resolution) {
        super(name, batteryLevel);
        this.resolution = resolution;
        this.photosTaken = 0;
    }

    public double getResolution() {
        return this.resolution;
    }

    public int getPhotosTaken() {
        return this.photosTaken;
    }

    @Override
    public void performMission() {
        if (this.state.isActive()) {
            System.out.println(this.name + ": Съемка территории с разрешением " + this.resolution + " м/пиксель");
            this.takePhoto();
            this.consumeBattery(0.08);
        } else {
            System.out.println(this.name + ": Не может выполнить съемку - не активен");
        }
    }

    private void takePhoto() {
        ++this.photosTaken;
        System.out.println(this.name + ": Снимок #" + this.photosTaken + " сделан!");
    }

    @Override
    public String toString() {
        return "ImagingSatellite{resolution=" + this.resolution + ", photosTaken=" + this.photosTaken + ", name='"
                + this.name + "', isActive=" + this.state.isActive() + ", batteryLevel=" + this.energy.getBatteryLevel()
                + "}";
    }
}
