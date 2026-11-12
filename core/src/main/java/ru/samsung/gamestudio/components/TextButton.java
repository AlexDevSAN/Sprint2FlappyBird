package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class TextButton {
    private BitmapFont font;
    private String text;
    private Texture texture;

    private int x, y;
    private int textX, textY;
    private int buttonWidth, buttonHeight;

    public TextButton(int x, int y, String text) {
        this.text = text;
        this.x = x;
        this.y = y;

        font = new BitmapFont();
        font.getData().setScale(5f);
        font.setColor(Color.WHITE);

        texture = new Texture("button_bg.png");
        buttonWidth = texture.getWidth();
        buttonHeight = texture.getHeight();

        // текст по центру кнопки: для этого меряем его ширину и высоту
        GlyphLayout gl = new GlyphLayout(font, text);
        textX = x + (buttonWidth - (int) gl.width) / 2;
        textY = y + (buttonHeight + (int) gl.height) / 2;
    }

    public boolean isHit(int tx, int ty) {
        return tx > x && tx < x + buttonWidth && ty > y && ty < y + buttonHeight;
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y, buttonWidth, buttonHeight);
        font.draw(batch, text, textX, textY);
    }

    public void dispose() {
        texture.dispose();
        font.dispose();
    }
}
