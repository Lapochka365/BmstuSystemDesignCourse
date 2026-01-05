package zoo;

import zoo.animals.*;
import zoo.core.*;
import zoo.employees.*;
import zoo.services.*;

public class Main {
    public static void main(String[] args) {
        Zoo zoo = new Zoo();

        System.out.println("\n- Приём сотрудников");

        Zookeeper ivan = new Zookeeper("Иван");
        Zookeeper maria = new Zookeeper("Мария");
        Veterinarian vasiliy = new Veterinarian("Василий");

        zoo.addEmployee(ivan);
        zoo.addEmployee(maria);
        zoo.addEmployee(vasiliy);

        System.out.println("\n- Создание вольеров");
        Enclosure predators = new Enclosure("Хищники");
        Enclosure birds = new Enclosure("Птичник");
        Enclosure terrarium = new Enclosure("Террариум");

        zoo.addEnclosure(predators);
        zoo.addEnclosure(birds);
        zoo.addEnclosure(terrarium);

        System.out.println("\n- Приём животных");

        Lion simba = new Lion("Симба", 40, 60, 9);
        Lion nala = new Lion("Нала", 50, 50, 9);
        Parrot kesha = new Parrot("Кеша", 85, 30, 8);
        Parrot gosha = new Parrot("Гоша", 70, 50, 8);
        Snake kaa = new Snake("Каа", 70, 20, 14);

        zoo.addAnimal(simba, predators, vasiliy);
        zoo.addAnimal(nala, predators, vasiliy);
        zoo.addAnimal(kesha, birds, vasiliy);
        zoo.addAnimal(gosha, birds, vasiliy);
        zoo.addAnimal(kaa, terrarium, vasiliy);

        System.out.println("В Зоопарке следующие животные: " + zoo.getAllAnimals());

        System.out.println("\n- Создание выставок");

        Exhibition africa = new Exhibition("Африка");
        africa.addEnclosure(predators);

        Exhibition tropical = new Exhibition("Тропики");
        tropical.addEnclosure(birds);
        tropical.addEnclosure(terrarium);

        zoo.addExhibition(africa);
        zoo.addExhibition(tropical);

        System.out.println("Текущие выставки: " + zoo.getExhibitions());

        System.out.println("\n- Медицинский осмотр");

        for (Animal animal : zoo.getAllAnimals()) {
            vasiliy.examine(animal);
        }

        System.out.println("\n- Кормление животных");

        for (Animal animal : predators.getAnimals()) {
            ivan.feed(animal);
        }
        for (Animal animal : birds.getAnimals()) {
            maria.feed(animal);
        }
        for (Animal animal : terrarium.getAnimals()) {
            ivan.feed(animal);
        }

        System.out.println("\n- Уборка вольеров");

        ivan.clean(predators);
        maria.clean(birds);
        ivan.clean(terrarium);

        ReportService reportService = new ReportService();
        reportService.generateReport(zoo);
    }
}
