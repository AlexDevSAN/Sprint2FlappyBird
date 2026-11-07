package ru.samsung.gamestudio.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.samsung.gamestudio.MyGdxGame;

public class Bird {
    public int x, y;
    public int speed;
    Texture[] framesArray;
    int frameCounter;

    int jumpHeight;
    final int maxHeightOfJump = 100;
    boolean jump;

    public int width;
    public int height;

    public int hitboxWidth;
    public int hitboxHeight;

    // skinFolder - папка с кадрами птицы, например "birdHero"
    public Bird(int x, int y, int speed, String skinFolder) {
        this.x = x;
        this.y = y;
        this.speed = speed;

        framesArray = new Texture[]{
            new Texture(skinFolder + "/bird0.png"),
            new Texture(skinFolder + "/bird1.png"),
            new Texture(skinFolder + "/bird2.png"),
            new Texture(skinFolder + "/bird1.png")
        };
        frameCounter = 0;

        this.width = (int)(framesArray[0].getWidth() * 0.7f);
        this.height = (int)(framesArray[0].getHeight() * 0.7f);

        this.hitboxWidth = (int)(this.width * 0.8f);
        this.hitboxHeight = (int)(this.height * 0.8f);
    }

    public void onClick() {
        jump = true;
        jumpHeight = maxHeightOfJump + y;
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

    public boolean isInField() {
        if (y < 0 || y > MyGdxGame.SCR_HEIGHT) {
            return false;
        }
        return true;
    }

    public void draw(Batch batch) {
        int frameMultiplier = 10;
        Texture currentFrame = framesArray[frameCounter / frameMultiplier];
        batch.draw(currentFrame, x, y, width, height);

        if (frameCounter++ == framesArray.length * frameMultiplier - 1) {
            frameCounter = 0;
        }
    }

    public void dispose() {
        for (Texture texture : framesArray) {
            texture.dispose();
        }
    }
}
