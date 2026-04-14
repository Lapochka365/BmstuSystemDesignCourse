package satellite;

import java.util.ArrayList;
import java.util.List;

public class SatelliteConstellation {
    private String constellationName;
    private List<Satellite> satellites;

    SatelliteConstellation(String name) {
        this.constellationName = name;
        this.satellites = new ArrayList<>();
        System.out.println("Создана спутниковая группировка: " + this.constellationName);
    }

    public void addSatellite(Satellite satellite) {
        this.satellites.add(satellite);
        System.out.println(satellite.name + " добавлен в группировку '" + this.constellationName + "'");
    }

    public void executeAllMissions() {
        System.out.println("Выполнение миссий группировки '" + constellationName + "'");
        for (Satellite satellite : this.satellites) {
            satellite.performMission();
        }
    }

    public List<Satellite> getSatellites() {
        return this.satellites;
    }

    public String getConstellationName() {
        return this.constellationName;
    }

    @Override
    public String toString() {
        return "SatelliteConstellation{constellationName='" + this.constellationName
                + "', satellites=" + this.satellites + "}";
    }
}
