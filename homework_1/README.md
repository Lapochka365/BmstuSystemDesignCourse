# ERP-система Московского зоопарка

## Предметная область

Программа моделирует базовую работу зоопарка:

- **Животные** (`Animal`) — различные виды с характеристиками (здоровье, голод, расписание кормления) и поведением (способ питания, лечения)
- **Сотрудники** (`Employee`) — работники зоопарка с разными обязанностями (смотрители кормят и убирают, ветеринары лечат)
- **Вольеры** (`Enclosure`) — места содержания животных
- **Выставки** (`Exhibition`) — тематические группировки вольеров
- **Отчётность** (`ReportService`) — формирование сводок по состоянию зоопарка

Система обеспечивает:
- Приём животных с обязательным медосмотром
- Кормление по потребности (на основе уровня голода)
- Лечение при необходимости (на основе уровня здоровья)
- Уборку вольеров
- Формирование отчётов (количество животных, расписание кормления, статистика)

---

## Принципы SOLID

### Single Responsibility Principle (SRP)

**Реализация в проекте:**

| Класс | Единственная ответственность |
|-------|------------------------------|
| `Animal` | Хранение состояния животного (здоровье, голод) |
| `Zookeeper` | Кормление животных и уборка вольеров |
| `Veterinarian` | Медицинские осмотры и лечение |
| `Enclosure` | Хранение животных в вольере |
| `Zoo` | Координация всех сущностей зоопарка |
| `ReportService` | Формирование отчётов |

**Пример:** `ReportService` отвечает только за генерацию отчётов. Если изменится формат отчёта — меняется только этот класс. Логика кормления или лечения его не затрагивает.

---

### Open/Closed Principle (OCP)

**Реализация в проекте:**

Добавление нового животного не требует изменения существующего кода. Например, создание класса Manul не требует каких-либо изменений
в остальных частях проекта.

**НЕ меняется:**
- `Zoo.java` — работает с любым `Animal`
- `Zookeeper.java` — кормит любой `Feedable`
- `Veterinarian.java` — лечит любой `Healable`
- `ReportService.java` — отчитывается по любым животным

Аналогично для сотрудников — добавление `Cleaner extends Employee` не затрагивает существующий код.

---

### Liskov Substitution Principle (LSP)

**Реализация в проекте:**

Любой наследник `Animal` корректно работает везде, где ожидается `Animal`:

```java
// Zoo работает с ЛЮБЫМИ животными единообразно
public void addAnimal(Animal animal, Enclosure enclosure, Veterinarian vet) {
    vet.examine(animal);        // Работает любой конкретный класс, наследующийся от Animal
    enclosure.addAnimal(animal);
}
```

- Все подклассы (`Lion`, `Parrot`, `Snake`) реализуют `feed()` и `heal()` без изменения семантики
- Методы `needsFeeding()` и `needsHealing()` работают единообразно (базовая реализация в `Animal`)
- Подклассы могут настраивать пороги (например, `Snake` имеет `hungerThreshold = 70`), но контракт не нарушается

---

### Interface Segregation Principle (ISP)

**Реализация в проекте:**

Вместо одного "толстого" интерфейса `AnimalCare` созданы узкоспециализированные:

```java
public interface Feedable {
    boolean needsFeeding();
    void feed();
}

public interface Healable {
    boolean needsHealing();
    void heal();
}

public interface Cleanable {
    void clean();
}

public interface Nameable {
    String getName();
}
```

**Как это используется:**

```java
// Zookeeper зависит ТОЛЬКО от Feedable и Cleanable
public class Zookeeper extends Employee {
    public <T extends Feedable & Nameable> void feed(T animal) { ... }
    public <T extends Cleanable & Nameable> void clean(T cleanable) { ... }
}

// Veterinarian зависит ТОЛЬКО от Healable
public class Veterinarian extends Employee {
    public <T extends Healable & Nameable> void examine(T animal) { ... }
}
```

- `Zookeeper` не знает о методах лечения — они ему не нужны
- `Veterinarian` не знает о методах кормления и уборки
- `Enclosure` реализует `Cleanable`, но не `Feedable` — вольер можно убирать, но не кормить.

---

### Dependency Inversion Principle (DIP)

**Реализация в проекте:**

Сотрудники зависят от интерфейсов, а не от конкретных классов:

```java
// Zookeeper НЕ зависит от Lion, Parrot, Snake
// Zookeeper зависит от абстракций в виде интерфейсов: Feedable, Cleanable, Nameable
public <T extends Feedable & Nameable> void feed(T animal) {
    if (animal.needsFeeding()) {
        animal.feed();  // Не важно, какой конкретно вид животного передается
    }
}
```

- `Zookeeper` → `Feedable` ← `Lion`, `Parrot`, `Snake` (все зависят от абстракции)
- Добавление `Manul` не требует изменения `Zookeeper`
- Можно протестировать `Zookeeper` с mock-объектом, реализующим `Feedable`
- Код `Zookeeper` не знает о существовании конкретных животных

**Package-private доступ как дополнительная защита:**
```java
// Enclosure.addAnimal() — package-private
void addAnimal(Animal animal) { ... }

// Только Zoo (тот же пакет) может добавлять животных
// Это гарантирует прохождение медосмотра при приёме
public void addAnimal(Animal animal, Enclosure enclosure, Veterinarian vet) {
    vet.examine(animal);         // Обязательный медосмотр
    enclosure.addAnimal(animal); // Только после осмотра
}
```

---

## Потенциальные проблемы расширения

### 1. Жёсткая привязка ветеринара к приёму животных

```java
public void addAnimal(Animal animal, Enclosure enclosure, Veterinarian vet)
```

**Проблема:** если появится требование принимать животных без осмотра (например, временное размещение), придётся модифицировать `Zoo` или создавать обходные пути.

**Решение:** вынести стратегию приёма в отдельный интерфейс:
```java
public interface AdmissionPolicy {
    void admit(Animal animal, Enclosure enclosure);
}
```

### 2. ReportService напрямую зависит от Zoo

```java
public void generateReport(Zoo zoo)
```

**Проблема:** если понадобится генерировать отчёт по части зоопарка (одной выставке), придётся менять сигнатуру или добавлять методы.

**Решение:** принимать более абстрактные коллекции:
```java
public void generateReport(List<Animal> animals, List<Employee> employees)
```

### 3. Отсутствие централизованного управления кормлением

**Проблема:** в `Main` вручную перебираются вольеры и вызывается `feed()`. При добавлении новых вольеров легко забыть обновить код.

**Решение:** создать `FeedingService`:
```java
public class FeedingService {
    public void feedAll(Zoo zoo, Zookeeper keeper) {
        zoo.getAllAnimals().forEach(keeper::feed);
    }
}
```

### 4. SpeciesType и EmployeePosition — enum

**Проблема:** добавление нового вида животного (например, `AMPHIBIAN`) или должности сотрудника (например, `CLEANER`) требует модификации enum.

**Решение:** для большей гибкости можно использовать строковые идентификаторы или отдельный класс.

---

## Преимущества введённых абстракций

### 1. Расширяемость без модификации

Интерфейсы `Feedable`, `Healable`, `Cleanable` позволяют добавлять новые сущности без изменения существующего кода:
- Новое животное (`Manul`) — просто наследуем `Animal`
- Новый сотрудник (`Cleaner`) — просто наследуем `Employee`
- Новый объект для уборки — реализуем `Cleanable`

### 2. Тестируемость

Зависимость от интерфейсов позволяет создавать mock-объекты:
```java
// Тест Zookeeper без реальных животных
Feedable mockAnimal = new MockFeedable();
zookeeper.feed(mockAnimal);
```

### 3. Читаемость кода

Сигнатуры методов явно показывают требования:
```java
// Сразу видно: нужен объект, который можно кормить и у которого есть имя
public <T extends Feedable & Nameable> void feed(T animal)
```

### 4. Слабая связанность (Low Coupling)

- `Zookeeper` не знает о `Lion` — только о `Feedable`
- `Veterinarian` не знает о `Parrot` — только о `Healable`
- Изменение внутренней реализации `Lion.feed()` не затрагивает `Zookeeper`

### 5. Гибкость с использованием интерфейсов

Классы могут реализовывать произвольные комбинации интерфейсов:
- `Animal` — `Feedable + Healable + Nameable`
- `Enclosure` — `Cleanable + Nameable`
- `Exhibition` — только `Nameable`

Это невозможно при использовании наследования вместо интерфейсов.

### 6. Соблюдение бизнес-правил через инкапсуляцию

Package-private `Enclosure.addAnimal()` гарантирует, что животные добавляются только через `Zoo.addAnimal()` с обязательным медосмотром по бизнес-требованию.

---

## Структура проекта

```
src/
└── zoo/
    ├── Cleanable.java            # Интерфейс: можно убирать
    ├── Feedable.java             # Интерфейс: можно кормить
    ├── Healable.java             # Интерфейс: можно лечить
    ├── Nameable.java             # Интерфейс: есть имя
    ├── Main.java                 # Демонстрация работы системы
    ├── animals/
    │   ├── Animal.java           # Абстрактный класс животного
    │   ├── Lion.java
    │   ├── Parrot.java
    │   ├── Snake.java
    │   └── SpeciesType.java      # Enum видов
    ├── core/
    │   ├── Enclosure.java        # Вольер
    │   ├── Exhibition.java       # Выставка
    │   └── Zoo.java              # Главный класс зоопарка
    ├── employees/
    │   ├── Employee.java         # Абстрактный сотрудник
    │   ├── EmployeePosition.java # Enum должностей
    │   ├── Veterinarian.java
    │   └── Zookeeper.java
    └── services/
        └── ReportService.java    # Отчёты
```

---

## Запуск

```bash
cd src
javac zoo/*.java zoo/**/*.java
java zoo.Main
```
