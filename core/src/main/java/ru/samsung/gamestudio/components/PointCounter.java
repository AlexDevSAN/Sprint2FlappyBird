package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class PointCounter {
    private int x, y;
    private BitmapFont font;
    private int shownPoints = -1; // какой счёт сейчас записан в text, -1 - ещё никакой
    private String text;

    public PointCounter(int x, int y) {
        this.x = x;
        this.y = y;

        font = new BitmapFont();
        font.getData().setScale(5f);
        font.setColor(Color.WHITE);
    }

    public void draw(Batch batch, int countOfPoints) {
        // строку собираем заново, только когда счёт изменился, а не каждый кадр
        if (countOfPoints != shownPoints) {
            shownPoints = countOfPoints;
            text = "Score: " + countOfPoints;
        }
        font.draw(batch, text, x, y);
    }

    public void dispose() {
        font.dispose();
    }
}
