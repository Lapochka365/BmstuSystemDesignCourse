public class Main {
    public static void main(String[] args) {
        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");
        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");
        CommunicationSatellite comm1 = new CommunicationSatellite("Связь-1", 0.85, 500.0);
        CommunicationSatellite comm2 = new CommunicationSatellite("Связь-2", 0.75, 1000.0);

        ImagingSatellite img1 = new ImagingSatellite("ДЗЗ-1", 0.92, 2.5);
        ImagingSatellite img2 = new ImagingSatellite("ДЗЗ-2", 0.45, 1.0);
        ImagingSatellite img3 = new ImagingSatellite("ДЗЗ-3", 0.15, 0.5);
        System.out.println("---------------------------------------------");
        SatelliteConstellation satConst = new SatelliteConstellation("RU Basic");
        System.out.println("---------------------------------------------");
        System.out.println("ФОРМИРОВАНИЕ ГРУППИРОВКИ:");
        System.out.println("-----------------------------------");
        satConst.addSatellite(comm1);
        satConst.addSatellite(comm2);
        satConst.addSatellite(img1);
        satConst.addSatellite(img2);
        satConst.addSatellite(img3);
        System.out.println("-----------------------------------");
        System.out.println(satConst.getSatellites());
        System.out.println("-----------------------------------");
        System.out.println("АКТИВАЦИЯ СПУТНИКОВ:");
        System.out.println("-------------------------");
        for (Satellite sat : satConst.getSatellites()) {
            sat.activate();
        }
        System.out.println("ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ " + satConst.getConstellationName().toUpperCase());
        System.out.println("============================================================");
        for (int i = 0; i < 1; i++) {
            satConst.executeAllMissions();
            System.out.println(satConst.getSatellites());
        }
    }
}
