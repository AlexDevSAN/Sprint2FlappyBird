package ru.samsung.gamestudio;

// уровень сложности: все его числа лежат здесь, в одном месте
// номер уровня: 0 - Easy, 1 - Medium, 2 - Hard
public class Difficulty {
    // номер в массиве = номер уровня
    final int[] tubeSpeeds = {2, 3, 4}; // пикселей за кадр
    // просвет должен быть меньше 620, иначе random.nextInt в Tube получит число <= 0 и игра упадёт
    final int[] gapHeights = {480, 400, 360};
    final int[] backgroundSpeeds = {1, 2, 3}; // фон чуть медленнее колонн, будто он дальше

    int level = 1; // сначала Medium, это прежние числа игры

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getTubeSpeed() {
        return tubeSpeeds[level];
    }

    public int getGapHeight() {
        return gapHeights[level];
    }

    public int getBackgroundSpeed() {
        return backgroundSpeeds[level];
    }
}
