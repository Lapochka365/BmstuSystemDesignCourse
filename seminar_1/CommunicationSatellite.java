public class CommunicationSatellite extends Satellite {
    private double bandwidth;

    CommunicationSatellite(String name, double batteryLevel, double bandwidth) {
        super(name, batteryLevel);
        this.bandwidth = bandwidth;
    }

    public double getBandwidth() {
        return this.bandwidth;
    }

    @Override
    public void performMission() {
        if (this.isActive) {
            System.out.println(this.name + ": Передача данных со скоростью " + this.bandwidth + " Мбит/с");
            this.sendData(this.bandwidth);
            this.consumeBattery(0.05);
        } else {
            System.out.println(this.name + ": Не может передать данные - не активен");
        }
    }

    private void sendData(double dataAmount) {
        System.out.println(this.name + ": Отправил " + dataAmount + " Мбит данных!");
    }

    @Override
    public String toString() {
        return "CommunicationSatellite{bandwidth=" + this.bandwidth + ", name='"
                + this.name + "', isActive=" + this.isActive + ", batteryLevel=" + this.batteryLevel + "}";
    }
}
