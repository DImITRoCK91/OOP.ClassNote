package ZabGU;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator; // класс, который помогает сортировать и сравнивать объекты
import java.util.List;


public class NoteDemo {

    public static void main(String[] args) throws IOException{
        demoSingleObject();
        demoArray();
        demoArrayList();
        demoFile();
    }

    // Один объект
    private static void demoSingleObject() {
        System.out.println("Один объект");
        Note def = new Note(); // создание ноты, заданной по умолчанию
        System.out.println("Note() = " + def);

        System.out.println();

        // Создание ноты через поля
        Note c4 = new Note(60, 4, 80);
        System.out.println("c4 = " + c4 + ", имя: " + c4.getName());
        System.out.printf("Частота: %.2f Гц%n", c4.frequency());

        System.out.println();

        Note g4 = c4.add(7); // Добавляем 7 полутонов
        System.out.println("c4.add(7) = " + g4 + ", интервал до неё: " + c4.intervalTo(g4) + " полутонов");
        System.out.println("c4.subtract(12) = " + c4.subtract(12));

        System.out.println();

        System.out.println("Равна ли " + c4.getName() + " и нота заданная по умолчанию: " + c4.equals(def));
        c4.setVelocity(100); // изменение состояния громкости через сеттер
        System.out.println("После setVelocity(100): " + c4);
        System.out.println("Равна ли " + c4.getName() + " и нота заданная по умолчанию: " + c4.equals(def));

        System.out.println();

        // Пример исключений
        try {
            new Note(200, 4, 80);
        } catch (IllegalArgumentException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
        try {
            c4.setDurationDen(0);
        } catch (IllegalArgumentException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }

    // Массив объектов.
    /// Конструкторы вызываются не при создании массива, а при каждом new Note()
    // После new Note[4] массив содержит 4 ссылки null, ни одного объекта ещё нет
    private static void demoArray() {
        System.out.println("\nМассив");
        Note[] melody = new Note[4];

        melody[0] = new Note(60, 4, 80); // здесь вызываем конструктор
        melody[1] = new Note(64, 4, 80);
        melody[2] = new Note(67, 4, 80);
        melody[3] = new Note(72, 2, 90);

        for (Note n : melody) {
            System.out.printf("%s - %.1f Гц%n", n, n.frequency());
        }
        System.out.println("Интервал melody[0] - melody[2]: " + melody[0].intervalTo(melody[2]) + " полутонов");

        Note[] literal = {new Note(), new Note(62, 4, 80)}; // здесь тоже два вызова конструктора
        System.out.println("Массив-литерал: " + literal[0] + ", " + literal[1]);
    }

    // ArrayList с заранее зарезервированной памятью
    private static void demoArrayList() {
        System.out.println("\nArrayList");
        ArrayList<Note> notes = new ArrayList<>(8);

        int[] pitches = {67, 60, 64, 62, 65};
        for (int p : pitches) {
            notes.add(new Note(p, 8, 70)); // здесь создаём объекты
        }

        for (Note n : notes) {
            n.setVelocity(n.getVelocity() + 10); // метод на каждом элементе
        }
        System.out.println("До сортировки:    " + notes);
        notes.sort(Comparator.comparingInt(Note::getPitch));
        System.out.println("После сортировки: " + notes);
        System.out.println("Содержит C4 (1/8, vel 80)? " + notes.contains(new Note(60, 8, 80)));
    }

    // Запись состояния в файл и чтение обратно
    private static void demoFile() throws IOException {
        System.out.println("\nФайл");
        List<Note> source = new ArrayList<>(3);
        source.add(new Note(60, 4, 80));
        source.add(new Note(64, 4, 85));
        source.add(new Note(67, 2, 90));

        // Запись
        Path file = Path.of("notes.txt");
        List<String> lines = new ArrayList<>(source.size());
        for (Note n : source) {
            lines.add(n.serialize());
        }
        Files.write(file, lines);
        System.out.println("Записано в " + file + ": " + lines);

        // Чтение
        List<Note> loaded = new ArrayList<>(source.size());
        for (String line : Files.readAllLines(file)) {
            loaded.add(Note.deserialize(line));
        }
        System.out.println("Прочитано: " + loaded);
        System.out.println("Списки совпадают? " + source.equals(loaded));
    }
}