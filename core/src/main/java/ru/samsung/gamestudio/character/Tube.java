package ru.samsung.gamestudio.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.samsung.gamestudio.MyGdxGame;
import java.util.Random;

public class Tube {
    Texture textureUpperTube;
    Texture textureDownTube;

    int x, gapY;
    int speed = 3;

    final int width = 200;
    final int height = 700;
    int gapHeight = 400; // без final: уровень сложности его меняет
    final int padding = 50;

    int distanceBetweenTubes;
    Random random;

    boolean isPointReceived;
    int closestDistance = MyGdxGame.SCR_HEIGHT; // как близко птица прошла к краю просвета, для достижений

    // картинки колонн передаём снаружи, на каждой карте они свои
    public Tube(int tubeCount, int tubeIdx, String upperTubePath, String downTubePath) {
        random = new Random();

        gapY = gapHeight / 2 + padding + random.nextInt(MyGdxGame.SCR_HEIGHT - 2 * (padding + gapHeight / 2));
        distanceBetweenTubes = (MyGdxGame.SCR_WIDTH + width) / (tubeCount - 1);
        x = distanceBetweenTubes * tubeIdx + MyGdxGame.SCR_WIDTH;

        textureUpperTube = new Texture(upperTubePath);
        textureDownTube = new Texture(downTubePath);

        isPointReceived = false;
    }

    // скорость и просвет текущего уровня, вызывается в начале каждой игры
    public void setDifficulty(int speed, int gapHeight) {
        this.speed = speed;
        this.gapHeight = gapHeight;
        // просвет стал другим, поэтому место просвета выбираем заново
        gapY = gapHeight / 2 + padding + random.nextInt(MyGdxGame.SCR_HEIGHT - 2 * (padding + gapHeight / 2));
    }

    public void move() {
        x -= speed;
        if (x < -width) {
            isPointReceived = false;
            closestDistance = MyGdxGame.SCR_HEIGHT; // новая пара - меряем заново
            x = MyGdxGame.SCR_WIDTH + distanceBetweenTubes;
            gapY = gapHeight / 2 + padding + random.nextInt(MyGdxGame.SCR_HEIGHT - 2 * (padding + gapHeight / 2));
        }
    }

    public boolean isHit(Bird bird) {
        int birdLeft = bird.x + (bird.width - bird.hitboxWidth) / 2;
        int birdRight = birdLeft + bird.hitboxWidth;
        int birdBottom = bird.y + (bird.height - bird.hitboxHeight) / 2;
        int birdTop = birdBottom + bird.hitboxHeight;

        int tubeLeft = x;
        int tubeRight = x + width;
        int lowerTubeTop = gapY - gapHeight / 2;
        int upperTubeBottom = gapY + gapHeight / 2;

        if (birdRight >= tubeLeft && birdLeft <= tubeRight) {
            if (birdBottom <= lowerTubeTop || birdTop >= upperTubeBottom) {
                return true;
            }
        }
        return false;
    }

    public boolean needAddPoint(Bird bird) {
        if (!isPointReceived && bird.x > x + width) {
            return true;
        }
        return false;
    }

    public void setPointReceived() {
        isPointReceived = true;
    }

    // пока птица между колоннами, запоминаем самое маленькое расстояние до края просвета
    public void updateClosestDistance(Bird bird) {
        int birdLeft = bird.x + (bird.width - bird.hitboxWidth) / 2;
        int birdRight = birdLeft + bird.hitboxWidth;
        int birdBottom = bird.y + (bird.height - bird.hitboxHeight) / 2;
        int birdTop = birdBottom + bird.hitboxHeight;

        if (birdRight >= x && birdLeft <= x + width) {
            int toLowerTube = birdBottom - (gapY - gapHeight / 2);
            int toUpperTube = (gapY + gapHeight / 2) - birdTop;
            if (toLowerTube < closestDistance) {
                closestDistance = toLowerTube;
            }
            if (toUpperTube < closestDistance) {
                closestDistance = toUpperTube;
            }
        }
    }

    public int getClosestDistance() {
        return closestDistance;
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
