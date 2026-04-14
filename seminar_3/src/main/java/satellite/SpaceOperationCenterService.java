package satellite;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SpaceOperationCenterService {
    private final ConstellationRepository repository;

    public SpaceOperationCenterService(ConstellationRepository repository) {
        this.repository = repository;
    }

    public void createAndSaveConstellation(String name) {
        SatelliteConstellation constellation = new SatelliteConstellation(name);
        repository.save(constellation);
        System.out.println("Спутниковая группировка создана: '" + name + "'");
    }

    public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
        SatelliteConstellation constellation = repository.findByName(constellationName);
        if (constellation != null) {
            constellation.addSatellite(satellite);
        } else {
            System.out.println("Группировка '" + constellationName + "' не найдена");
        }
    }

    public void executeConstellationMission(String constellationName) {
        SatelliteConstellation constellation = repository.findByName(constellationName);
        if (constellation != null) {
            constellation.executeAllMissions();
        } else {
            System.out.println("Группировка '" + constellationName + "' не найдена");
        }
    }

    public void activateAllSatellites(String constellationName) {
        SatelliteConstellation constellation = repository.findByName(constellationName);
        if (constellation != null) {
            System.out.println("Активация спутников в группировке '" + constellationName + "'");
            for (Satellite sat : constellation.getSatellites()) {
                sat.activate();
            }
        } else {
            System.out.println("Группировка '" + constellationName + "' не найдена");
        }
    }

    public void showConstellationStatus(String constellationName) {
        SatelliteConstellation constellation = repository.findByName(constellationName);
        if (constellation != null) {
            List<Satellite> satellites = constellation.getSatellites();
            System.out.println("Статус группировки '" + constellationName + "' (кол-во спутников: "
                    + satellites.size() + ")");
            System.out.println(satellites);
        } else {
            System.out.println("Группировка '" + constellationName + "' не найдена");
        }
    }
}
