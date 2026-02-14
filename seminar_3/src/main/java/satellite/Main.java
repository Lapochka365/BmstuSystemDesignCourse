package satellite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        ConstellationRepository repository = context.getBean(ConstellationRepository.class);
        SpaceOperationCenterService service = context.getBean(SpaceOperationCenterService.class);

        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");
        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ");
        System.out.println("---------------------------------------------");
        CommunicationSatellite comm1 = new CommunicationSatellite("Связь-1", 0.85, 500.0);
        CommunicationSatellite comm2 = new CommunicationSatellite("Связь-2", 0.75, 1000.0);

        ImagingSatellite img1 = new ImagingSatellite("ДЗЗ-1", 0.92, 2.5);
        ImagingSatellite img2 = new ImagingSatellite("ДЗЗ-2", 0.45, 1.0);
        ImagingSatellite img3 = new ImagingSatellite("ДЗЗ-3", 0.15, 0.5);
        System.out.println("---------------------------------------------");
        service.createAndSaveConstellation("RU Basic 1");
        service.createAndSaveConstellation("RU Basic 2");
        System.out.println("---------------------------------------------");
        System.out.println("ФОРМИРОВАНИЕ ГРУППИРОВКИ RU Basic 1");
        System.out.println("-----------------------------------");
        service.addSatelliteToConstellation("RU Basic 1", comm1);
        service.addSatelliteToConstellation("RU Basic 1", img1);
        service.addSatelliteToConstellation("RU Basic 1", img2);
        System.out.println("ФОРМИРОВАНИЕ ГРУППИРОВКИ RU Basic 2");
        System.out.println("-----------------------------------");
        service.addSatelliteToConstellation("RU Basic 2", comm2);
        service.addSatelliteToConstellation("RU Basic 2", img3);
        System.out.println("-----------------------------------");
        service.showConstellationStatus("RU Basic 1");
        service.showConstellationStatus("RU Basic 2");
        System.out.println("-----------------------------------");
        System.out.println("АКТИВАЦИЯ СПУТНИКОВ");
        System.out.println("-------------------------");
        service.activateAllSatellites("RU Basic 1");
        service.activateAllSatellites("RU Basic 2");
        System.out.println("ВЫПОЛНЕНИЕ МИССИЙ");
        System.out.println("============================================================");
        service.executeConstellationMission("RU Basic 1");
        service.executeConstellationMission("RU Basic 2");

        service.showConstellationStatus("RU Basic 1");
        service.showConstellationStatus("RU Basic 2");

        System.out.println(repository.getAll());

        context.close();
    }
}
