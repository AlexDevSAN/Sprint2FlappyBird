package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import ru.samsung.gamestudio.MyGdxGame;

// достижения за одну игру: игра сообщает события, а здесь решаем, что открыто
public class AchievementTracker {
    // номера достижений в массиве NAMES
    private static final int CLOSE_SHAVE = 0;
    private static final int HAT_TRICK = 1;
    private static final int PERFECT_TEN = 2;
    private static final int CAVE_EXPLORER = 3;
    private static final int KAMIKAZE = 4;
    private static final int INSANITY = 5; // его даёт пасхалка
    private static final String[] NAMES = {"Close Shave", "Hat Trick", "Perfect Ten", "Cave Explorer", "Kamikaze",
        "Definition of Insanity"};

    private static final int CLOSE_SHAVE_DISTANCE = 8; // прошёл к краю просвета не дальше этого - Close Shave
    private static final int CLOSE_SHAVES_FOR_HAT_TRICK = 3;
    private static final int POINTS_FOR_PERFECT_TEN = 10;
    private static final int POINTS_FOR_CAVE_EXPLORER = 5;

    private static final int POPUP_X = 20;
    private static final int POPUP_Y = 700;
    private static final int LIST_X = 750;
    private static final int LIST_Y = 440;
    private static final int POPUP_FRAMES = 120; // 2 секунды при 60 кадрах в секунду

    private boolean[] isUnlocked; // какие достижения уже открыты в этой игре
    private int[] unlockOrder; // номера открытых достижений по порядку, это же очередь плашек
    private int unlockedCount;
    private int closeShaveCount; // сколько раз прошли впритирку

    private String[] popupTexts; // тексты плашек собираем один раз, а не каждый кадр
    private String listText; // список для экрана рестарта, дописываем при каждом новом достижении

    private BitmapFont font;
    private int shownCount; // сколько плашек уже показали
    private int popupTimer; // сколько кадров висит текущая плашка
    private boolean isNewPopup;

    public AchievementTracker() {
        isUnlocked = new boolean[NAMES.length];
        unlockOrder = new int[NAMES.length];
        popupTexts = new String[NAMES.length];
        for (int i = 0; i < NAMES.length; i++) {
            popupTexts[i] = "Achievement: " + NAMES[i];
        }

        font = new BitmapFont();
        font.getData().setScale(3f); // мельче счёта, у него 5
        font.setColor(Color.GOLD);

        reset();
    }

    // новая игра - всё с нуля
    public void reset() {
        for (int i = 0; i < NAMES.length; i++) {
            isUnlocked[i] = false;
        }
        unlockedCount = 0;
        closeShaveCount = 0;
        shownCount = 0;
        popupTimer = 0;
        isNewPopup = false;
        listText = "No achievements";
    }

    public void onPoint(int gamePoints, int mapIndex) {
        if (gamePoints >= POINTS_FOR_PERFECT_TEN) {
            unlock(PERFECT_TEN);
        }
        if (mapIndex == MyGdxGame.MAP_CAVE && gamePoints >= POINTS_FOR_CAVE_EXPLORER) {
            unlock(CAVE_EXPLORER);
        }
    }

    // closestDistance - как близко птица была к краю просвета, пока пролетала пару колонн
    public void onTubePassed(int closestDistance) {
        if (closestDistance <= CLOSE_SHAVE_DISTANCE) {
            closeShaveCount++;
            unlock(CLOSE_SHAVE);
            if (closeShaveCount >= CLOSE_SHAVES_FOR_HAT_TRICK) {
                unlock(HAT_TRICK);
            }
        }
    }

    public void onGameOver(int gamePoints) {
        if (gamePoints == 0) {
            unlock(KAMIKAZE);
        }
    }

    public void onEasterEgg() {
        unlock(INSANITY);
    }

    private void unlock(int index) {
        if (isUnlocked[index]) {
            return; // за одну игру второй раз не даём
        }
        isUnlocked[index] = true;
        unlockOrder[unlockedCount] = index; // встаёт в очередь на плашку
        unlockedCount++;

        // первое достижение начинает список, остальные дописываются с новой строки
        if (unlockedCount == 1) {
            listText = "Achievements:";
        }
        listText += "\n" + NAMES[index];
    }

    // true один раз, когда всплыла новая плашка: по нему играет мелодия достижения
    public boolean isNewAchievement() {
        if (isNewPopup) {
            isNewPopup = false;
            return true;
        }
        return false;
    }

    public void drawPopup(Batch batch) {
        if (shownCount == unlockedCount) {
            return; // показывать нечего
        }
        if (popupTimer == 0) {
            isNewPopup = true;
        }
        font.draw(batch, popupTexts[unlockOrder[shownCount]], POPUP_X, POPUP_Y);
        popupTimer++;
        if (popupTimer == POPUP_FRAMES) {
            popupTimer = 0;
            shownCount++; // эта плашка своё отвисела, дальше следующая из очереди
        }
    }

    public void drawList(Batch batch) {
        font.draw(batch, listText, LIST_X, LIST_Y);
    }

    public void dispose() {
        font.dispose();
    }
}
