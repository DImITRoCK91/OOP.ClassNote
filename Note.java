package ZabGU;

import java.util.Objects;

/**
 * Музыкальная нота: высота, длительность и громкость.
 * Состояние объекта - три поля:
 * Название ноты и частота вычисляются из полей, поэтому сеттеров для них нет.
 **/
public class Note {

    // Константы (диапазон)
    //
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 127;

    // Названия нот
    private static final String[] NAMES =
            {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    // Наши основные поля
    private int pitch;        // высота звука, 0..127
    private int durationDen;  // длительность ноты
    private int velocity;     // громкость ноты, 0..127
    // Название ноты и частота вычисляются из полей, поэтому сеттеров для них нет

    /// Конструкторы:
    // Конструктор -- это метод инициализирующий объект

    // Создаёт ноту по умолчанию: C4, четвертная, громкость 80.
    public Note() {
        this(60, 4, 80);
    }

    // Создаёт ноту с заданными параметрами.
    public Note(int pitch, int durationDen, int velocity) {
        setPitch(pitch); // высота (MIDI-номер), 0..127
        setDurationDen(durationDen); // знаменатель длительности (4 = четвертная), больше 0
        setVelocity(velocity); // громкость, 0..127
    }

    /// Геттеры и сеттеры
    // Геттер - метод, который возвращает значение поля объекта
    // Сеттер - метод, который задаёт или изменяет это значение

    // Возвращает высоту звука (MIDI-номер)
    public int getPitch() {
        return pitch;
    }

    // Задаёт новую высоту, 0..127
    public void setPitch(int pitch) {
        if (pitch < MIN_VALUE || pitch > MAX_VALUE) {
            // IllegalArgumentException если значение вне диапазона
            throw new IllegalArgumentException("Высота должна быть 0..127: " + pitch);
        }
        // this устраняет конфликт имён, когда параметр называется так же, как поле
        this.pitch = pitch;
    }

    // Возвращает знаменатель длительности (4 означает 1/4)
    public int getDurationDen() {
        return durationDen;
    }

    // Задаёт durationDen новый знаменатель длительности, больше 0
    public void setDurationDen(int durationDen) {
        if (durationDen <= 0) {
            // IllegalArgumentException если значение не положительное
            throw new IllegalArgumentException("Знаменатель длительности должен быть > 0: " + durationDen);
        }
        this.durationDen = durationDen;
    }

    // Возвращает громкость 0..127 */
    public int getVelocity() {
        return velocity;
    }

    // Возвращает velocity новая громкость, 0..127
    public void setVelocity(int velocity) {
        if (velocity < MIN_VALUE || velocity > MAX_VALUE) {
            // IllegalArgumentException если значение вне диапазона
            throw new IllegalArgumentException("Громкость должна быть 0..127: " + velocity);
        }
        this.velocity = velocity;
    }

    /// Вычисления

    // Возвращает название ноты с октавой, например "C#4" (для pitch 60 - "C4")
    public String getName() {
        return NAMES[pitch % 12] + (pitch / 12 - 1);
    }

    // Возвращает частоту в герцах, строй A4 = 440 Гц
    //
    public double frequency() {
        return 440.0 * Math.pow(2.0, (pitch - 69) / 12.0);
    }

    // Интервал в полутонах от этой ноты до другой (может быть отрицательным)
    public int intervalTo(Note other) {
        Objects.requireNonNull(other, "Нота не задана");
        return other.pitch - pitch;
    }

    /// Операторы (возвращают новую ноту)

    /**
     * Транспонирование вверх
     * semitones - число полутонов
     * Возвращает новую ноту, длительность и громкость сохраняются
     **/
    public Note add(int semitones) {
        return new Note(pitch + semitones, durationDen, velocity);
    }

    // Транспонирование вниз
    public Note subtract(int semitones) {
        return new Note(pitch - semitones, durationDen, velocity);
    }

    /// Сравнение и строка

    // Ноты равны, если совпадают все три поля
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Note n = (Note) o;
        return pitch == n.pitch && durationDen == n.durationDen && velocity == n.velocity;
    }

    // Возвращает строку вида C#4 (1/4, vel 80)
    @Override
    public String toString() {
        return getName() + " (1/" + durationDen + ", vel " + velocity + ")";
    }


    /// Запись и чтение, работа со строкой

    // Возвращается строка для файла: "pitch;durationDen;velocity", например "60;4;80"
    public String serialize() {
        return pitch + ";" + durationDen + ";" + velocity;
    }

    /**
     * Создаёт ноту из строки, записанной методом serialize()
     * строка вида "60;4;80"
     **/
    public static Note deserialize(String line) {
        Objects.requireNonNull(line, "Строка не задана"); // Метод из java.util, который проверяет не равен ли объект null
        String[] parts = line.trim().split(";"); // line.trim() - удаляет все пробельные символы в самом начале и в самом конце строки line
        // .split(";") - разрезает очищенную строку на части везде, где встречается точка с запятой
        if (parts.length != 3) { // Проверяем кол-во параметров
            // IllegalArgumentException если формат неверный
            throw new IllegalArgumentException("Ожидался формат pitch;durationDen;velocity: \"" + line + "\"");
        }
        try { // Преобразуем текст в числа
            return new Note(
                    Integer.parseInt(parts[0].trim()),
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim()));
        } catch (NumberFormatException e) {
            // IllegalArgumentException если не число; причина e сохраняется
            throw new IllegalArgumentException("Не число в строке \"" + line + "\"", e);
        }
    }

}