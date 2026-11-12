package ru.samsung.gamestudio;

// уровень сложности: все его числа лежат здесь, в одном месте
// номер уровня: 0 - Easy, 1 - Medium, 2 - Hard
public class Difficulty {
    // номер в массиве = номер уровня
    public static final String[] LEVEL_NAMES = {"Easy", "Medium", "Hard"};
    private static final int[] TUBE_SPEEDS = {2, 3, 4}; // пикселей за кадр
    // просвет должен быть меньше 620, иначе random.nextInt в Tube получит число <= 0 и игра упадёт
    private static final int[] GAP_HEIGHTS = {480, 400, 360};
    private static final int[] BACKGROUND_SPEEDS = {1, 2, 3}; // фон чуть медленнее колонн, будто он дальше

    private int level = 1; // сначала Medium, это прежние числа игры

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTubeSpeed() {
        return TUBE_SPEEDS[level];
    }

    public int getGapHeight() {
        return GAP_HEIGHTS[level];
    }

    public int getBackgroundSpeed() {
        return BACKGROUND_SPEEDS[level];
    }
}
