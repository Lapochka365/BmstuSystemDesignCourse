package satellite;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class ConstellationRepository {
    private Map<String, SatelliteConstellation> constellations = new HashMap<>();

    public void save(SatelliteConstellation constellation) {
        this.constellations.put(constellation.getConstellationName(), constellation);
    }

    public SatelliteConstellation findByName(String name) {
        return this.constellations.get(name);
    }

    public Map<String, SatelliteConstellation> getAll() {
        return this.constellations;
    }

    public void deleteByName(String name) {
        this.constellations.remove(name);
    }

    public boolean exists(String name) {
        return this.constellations.containsKey(name);
    }

}
