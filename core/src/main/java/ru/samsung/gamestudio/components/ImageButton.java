package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

// кнопка-картинка с подписью снизу, для выбора карты и птицы
public class ImageButton {
    private static final int TEXT_GAP = 20; // на столько пикселей подпись ниже картинки

    private Texture texture;
    private BitmapFont font;
    private String text;

    private int x, y;
    private int width, height;
    private int textX, textY;

    public ImageButton(String texturePath, String text, int centerX, int y, int height) {
        this.text = text;
        this.y = y;
        this.height = height;

        texture = new Texture(texturePath);
        // ширину считаем по пропорциям картинки, чтобы она не сплющилась
        width = texture.getWidth() * height / texture.getHeight();
        x = centerX - width / 2;

        font = new BitmapFont();
        font.getData().setScale(3f);
        font.setColor(Color.WHITE);

        GlyphLayout gl = new GlyphLayout(font, text);
        textX = centerX - (int) gl.width / 2;
        textY = y - TEXT_GAP;
    }

    public boolean isHit(int tx, int ty) {
        return tx > x && tx < x + width && ty > y && ty < y + height;
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y, width, height);
        font.draw(batch, text, textX, textY);
    }

    public void dispose() {
        texture.dispose();
        font.dispose();
    }
}
