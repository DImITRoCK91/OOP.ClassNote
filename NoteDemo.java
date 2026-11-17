package ZabGU;

public class NoteDemo {

    public static void main(String[] args){
        demoSingleObject();
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
}