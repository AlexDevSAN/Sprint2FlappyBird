package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.samsung.gamestudio.MyGdxGame;

// бесконечный фон из двух одинаковых половинок, которые едут влево друг за другом
public class MovingBackground {
    private static final int OVERLAP = 2; // половинки рисуем чуть шире, чтобы они заходили друг на друга

    private Texture texture;
    private int texture1X, texture2X;
    private int speed = 2;

    public MovingBackground(String pathToTexture) {
        texture = new Texture(pathToTexture);
        texture1X = 0;
        texture2X = MyGdxGame.SCR_WIDTH;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void move() {
        texture1X -= speed;
        texture2X -= speed;

        // уехавшую половинку ставим вплотную за второй, тогда щели нет при любой скорости
        if (texture1X <= -MyGdxGame.SCR_WIDTH) {
            texture1X = texture2X + MyGdxGame.SCR_WIDTH;
        }
        if (texture2X <= -MyGdxGame.SCR_WIDTH) {
            texture2X = texture1X + MyGdxGame.SCR_WIDTH;
        }
    }

    public void draw(Batch batch) {
        batch.draw(texture, texture1X, 0, MyGdxGame.SCR_WIDTH + OVERLAP, MyGdxGame.SCR_HEIGHT);
        batch.draw(texture, texture2X, 0, MyGdxGame.SCR_WIDTH + OVERLAP, MyGdxGame.SCR_HEIGHT);
    }

    public void dispose() {
        texture.dispose();
    }
}
