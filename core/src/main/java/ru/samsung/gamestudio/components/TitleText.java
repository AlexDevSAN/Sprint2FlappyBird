package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import ru.samsung.gamestudio.MyGdxGame;

// заголовок экрана: крупный текст по центру сверху
public class TitleText {
    private static final int Y = 650;

    private BitmapFont font;
    private String text;
    private int x;

    public TitleText(String text) {
        this.text = text;

        font = new BitmapFont();
        font.getData().setScale(4f);
        font.setColor(Color.WHITE);

        // по центру: (ширина экрана - ширина текста) / 2
        GlyphLayout gl = new GlyphLayout(font, text);
        x = (MyGdxGame.SCR_WIDTH - (int) gl.width) / 2;
    }

    public void draw(Batch batch) {
        font.draw(batch, text, x, Y);
    }

    public void dispose() {
        font.dispose();
    }
}
