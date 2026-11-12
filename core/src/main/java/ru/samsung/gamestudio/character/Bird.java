package ru.samsung.gamestudio.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.samsung.gamestudio.MyGdxGame;

public class Bird {
    private static final float SCALE = 0.7f; // на экране птица 70% от картинки
    private static final float HITBOX_SCALE = 0.8f; // хитбокс 80% от птицы: прозрачные края не считаются
    private static final int MAX_HEIGHT_OF_JUMP = 100; // на столько пикселей поднимает один взмах
    private static final int FRAME_MULTIPLIER = 10; // столько кадров игры держится один кадр анимации

    private int x, y;
    private int startY; // откуда птица начинает каждую игру
    private int speed;
    private Texture[] framesArray;
    private int frameCounter;

    private int jumpHeight;
    private boolean jump;

    private int width, height;
    private int hitboxWidth, hitboxHeight;

    // skinFolder - папка с кадрами птицы, например "birdHero"
    public Bird(int x, int startY, int speed, String skinFolder) {
        this.x = x;
        this.startY = startY;
        this.speed = speed;

        framesArray = new Texture[]{
            new Texture(skinFolder + "/bird0.png"),
            new Texture(skinFolder + "/bird1.png"),
            new Texture(skinFolder + "/bird2.png"),
            new Texture(skinFolder + "/bird1.png")
        };

        width = (int) (framesArray[0].getWidth() * SCALE);
        height = (int) (framesArray[0].getHeight() * SCALE);
        hitboxWidth = (int) (width * HITBOX_SCALE);
        hitboxHeight = (int) (height * HITBOX_SCALE);

        reset();
    }

    // перед каждой новой игрой: птица на старте, прыжок из прошлой игры забыт
    public void reset() {
        y = startY;
        jump = false;
        jumpHeight = 0;
        frameCounter = 0;
    }

    public void onClick() {
        jump = true;
        jumpHeight = MAX_HEIGHT_OF_JUMP + y;
    }

    public void fly() {
        if (y >= jumpHeight) {
            jump = false;
        }

        if (jump) {
            y += speed;
        } else {
            y -= speed;
        }
    }

    // в поле, пока хитбокс не вышел за нижний или верхний край экрана
    public boolean isInField() {
        return getHitboxBottom() >= 0 && getHitboxTop() <= MyGdxGame.SCR_HEIGHT;
    }

    public void draw(Batch batch) {
        Texture currentFrame = framesArray[frameCounter / FRAME_MULTIPLIER];
        batch.draw(currentFrame, x, y, width, height);

        frameCounter++;
        if (frameCounter == framesArray.length * FRAME_MULTIPLIER) {
            frameCounter = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // хитбокс стоит по центру птицы
    public int getHitboxLeft() {
        return x + (width - hitboxWidth) / 2;
    }

    public int getHitboxRight() {
        return getHitboxLeft() + hitboxWidth;
    }

    public int getHitboxBottom() {
        return y + (height - hitboxHeight) / 2;
    }

    public int getHitboxTop() {
        return getHitboxBottom() + hitboxHeight;
    }

    public void dispose() {
        for (Texture texture : framesArray) {
            texture.dispose();
        }
    }
}
