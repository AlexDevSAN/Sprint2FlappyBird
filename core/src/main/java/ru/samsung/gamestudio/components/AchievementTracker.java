package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

// достижения за одну игру: игра сообщает события, а здесь решаем, что открыто
public class AchievementTracker {
    // номера достижений в массиве names
    static final int CLOSE_SHAVE = 0;
    static final int HAT_TRICK = 1;
    static final int PERFECT_TEN = 2;
    static final int CAVE_EXPLORER = 3;
    static final int KAMIKAZE = 4;

    final String[] names = {"Close Shave", "Hat Trick", "Perfect Ten", "Cave Explorer", "Kamikaze"};

    final int closeShaveDistance = 8; // прошёл к краю просвета не дальше этого - Close Shave
    final int closeShavesForHatTrick = 3;
    final int pointsForPerfectTen = 10;
    final int pointsForCaveExplorer = 5;
    final int caveMapIndex = 1; // номер пещеры, как в ScreenGame

    boolean[] isUnlocked; // какие достижения уже открыты в этой игре
    int[] unlockOrder; // номера открытых достижений по порядку, это же очередь плашек
    int unlockedCount;
    int closeShaveCount; // сколько раз прошли впритирку

    BitmapFont font;
    final int popupX = 20;
    final int popupY = 700;
    final int listX = 750;
    final int listY = 440;

    final int popupFrames = 120; // 2 секунды при 60 кадрах в секунду
    int shownCount; // сколько плашек уже показали
    int popupTimer; // сколько кадров висит текущая плашка
    boolean isNewPopup;

    public AchievementTracker() {
        isUnlocked = new boolean[names.length];
        unlockOrder = new int[names.length];

        font = new BitmapFont();
        font.getData().setScale(3f); // мельче счёта, у него 5
        font.setColor(Color.GOLD);

        reset();
    }

    // новая игра - всё с нуля
    public void reset() {
        for (int i = 0; i < names.length; i++) {
            isUnlocked[i] = false;
        }
        unlockedCount = 0;
        closeShaveCount = 0;
        shownCount = 0;
        popupTimer = 0;
        isNewPopup = false;
    }

    public void onPoint(int gamePoints, int mapIndex) {
        if (gamePoints >= pointsForPerfectTen) {
            unlock(PERFECT_TEN);
        }
        if (mapIndex == caveMapIndex && gamePoints >= pointsForCaveExplorer) {
            unlock(CAVE_EXPLORER);
        }
    }

    // closestDistance - как близко птица была к краю просвета, пока пролетала пару колонн
    public void onTubePassed(int closestDistance) {
        if (closestDistance <= closeShaveDistance) {
            closeShaveCount++;
            unlock(CLOSE_SHAVE);
            if (closeShaveCount >= closeShavesForHatTrick) {
                unlock(HAT_TRICK);
            }
        }
    }

    public void onGameOver(int gamePoints) {
        if (gamePoints == 0) {
            unlock(KAMIKAZE);
        }
    }

    void unlock(int index) {
        if (isUnlocked[index]) {
            return; // за одну игру второй раз не даём
        }
        isUnlocked[index] = true;
        unlockOrder[unlockedCount] = index; // встаёт в очередь на плашку
        unlockedCount++;
    }

    // текст плашки или null, если показывать нечего
    public String getPopupText() {
        if (shownCount == unlockedCount) {
            return null;
        }
        return "Achievement: " + names[unlockOrder[shownCount]];
    }

    // список для экрана рестарта, \n - перенос строки
    public String getListText() {
        if (unlockedCount == 0) {
            return "No achievements";
        }
        String text = "Achievements:";
        for (int i = 0; i < unlockedCount; i++) {
            text += "\n" + names[unlockOrder[i]];
        }
        return text;
    }

    // true один раз, когда всплыла новая плашка (сюда подключат звук)
    public boolean isNewAchievement() {
        if (isNewPopup) {
            isNewPopup = false;
            return true;
        }
        return false;
    }

    public void drawPopup(Batch batch) {
        String text = getPopupText();
        if (text == null) {
            return;
        }
        if (popupTimer == 0) {
            isNewPopup = true;
        }
        font.draw(batch, text, popupX, popupY);
        popupTimer++;
        if (popupTimer == popupFrames) {
            popupTimer = 0;
            shownCount++; // эта плашка своё отвисела, дальше следующая из очереди
        }
    }

    public void drawList(Batch batch) {
        font.draw(batch, getListText(), listX, listY);
    }

    public void dispose() {
        font.dispose();
    }
}
