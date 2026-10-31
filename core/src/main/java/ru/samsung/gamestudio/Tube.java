package ru.samsung.gamestudio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.Random;

public class Tube {
    Texture textureUpperTube;
    Texture textureDownTube;

    int x, gapY;
    int speed = 3;

    final int width = 200;
    final int height = 700;
    final int gapHeight = 400;
    final int padding = 50;

    int distanceBetweenTubes;

    Random random;

    public Tube(int tubeCount, int tubeIdx) {
        random = new Random();

        gapY = gapHeight / 2 + padding + random.nextInt(MyGdxGame.SCR_HEIGHT - 2 * (padding + gapHeight / 2));
        distanceBetweenTubes = (MyGdxGame.SCR_WIDTH + width) / (tubeCount - 1);
        x = distanceBetweenTubes * tubeIdx + MyGdxGame.SCR_WIDTH;

        textureUpperTube = new Texture("Tube/tube_flipped.png");
        textureDownTube = new Texture("Tube/tube.png");
    }

    public void move() {
        x -= speed;
        if (x < -width) {
            x = MyGdxGame.SCR_WIDTH + distanceBetweenTubes;
            gapY = gapHeight / 2 + padding + random.nextInt(MyGdxGame.SCR_HEIGHT - 2 * (padding + gapHeight / 2));
        }
    }

    public boolean isHit(Bird bird) {
        // Вычисляем координаты уменьшенного хитбокса строго по центру птички
        int birdLeft = bird.x + (bird.width - bird.hitboxWidth) / 2;
        int birdRight = birdLeft + bird.hitboxWidth;
        int birdBottom = bird.y + (bird.height - bird.hitboxHeight) / 2;
        int birdTop = birdBottom + bird.hitboxHeight;

        // Границы труб
        int tubeLeft = x;
        int tubeRight = x + width;
        int lowerTubeTop = gapY - gapHeight / 2;
        int upperTubeBottom = gapY + gapHeight / 2;

        // Проверяем пересечение по оси X
        if (birdRight >= tubeLeft && birdLeft <= tubeRight) {
            // Столкновение с нижней трубой
            if (birdBottom <= lowerTubeTop) {
                return true;
            }
            // Столкновение с верхней трубой
            if (birdTop >= upperTubeBottom) {
                return true;
            }
        }
        return false;
    }

    public void draw(Batch batch) {
        batch.draw(textureUpperTube, x, gapY + gapHeight / 2, width, height);
        batch.draw(textureDownTube, x, gapY - gapHeight / 2 - height, width, height);
    }

    public void dispose() {
        textureDownTube.dispose();
        textureUpperTube.dispose();
    }
}
