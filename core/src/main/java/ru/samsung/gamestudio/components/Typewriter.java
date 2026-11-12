package ru.samsung.gamestudio.components;

// печатает реплику по одной букве, как в старых играх
public class Typewriter {
    private static final int LETTER_PAUSE = 1; // после буквы ждём 1 кадр: буква появляется раз в 2 кадра
    private static final int COMMA_PAUSE = 8; // после запятой, точки, вопроса и восклицания пауза короткая
    private static final int DOTS_PAUSE = 24; // после многоточия длинная, для драмы
    private static final String COMMA_LETTERS = ",.?!"; // знаки с короткой паузой

    private String line; // вся реплика
    private String shownText; // часть, которая уже напечатана
    private int shownCount; // сколько букв напечатано
    private int pause; // сколько кадров ещё ждать до следующей буквы

    public Typewriter() {
        start("");
    }

    public void start(String line) {
        this.line = line;
        shownText = "";
        shownCount = 0;
        pause = 0;
    }

    // один кадр печати. true, если в этом кадре появилась буква: на неё пищит звук
    public boolean update() {
        if (isFinished()) {
            return false;
        }
        if (pause > 0) {
            pause--;
            return false;
        }

        String letter = line.substring(shownCount, shownCount + 1);
        shownCount++;
        shownText = line.substring(0, shownCount); // строку собираем, только когда добавилась буква

        if (letter.equals("…")) { // многоточие здесь один знак, а не три точки
            pause = DOTS_PAUSE;
        } else if (COMMA_LETTERS.contains(letter)) {
            pause = COMMA_PAUSE;
        } else {
            pause = LETTER_PAUSE;
        }
        return !letter.equals(" ") && !letter.equals("\n"); // на пробел и перенос строки не пищим
    }

    public boolean isFinished() {
        return shownCount == line.length();
    }

    public String getShownText() {
        return shownText;
    }
}
