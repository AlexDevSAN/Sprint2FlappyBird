package ru.samsung.gamestudio.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.samsung.gamestudio.MyGdxGame;
import java.util.Random;

public class Tube {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 700;
    private static final int PADDING = 50; // просвет не подходит к краю экрана ближе этого

    private Texture textureUpperTube;
    private Texture textureDownTube;

    private int x, gapY;
    private int speed = 3;
    private int gapHeight = 400;

    private int tubeIdx; // номер колонны: по нему считаем, где она стоит в начале игры
    private int distanceBetweenTubes;
    private Random random;

    private boolean isPointReceived;
    private int closestDistance; // как близко птица прошла к краю просвета, для достижений

    // колонны создаются один раз, а перед каждой игрой их сбрасывает reset()
    public Tube(int tubeCount, int tubeIdx) {
        this.tubeIdx = tubeIdx;
        random = new Random();
        distanceBetweenTubes = (MyGdxGame.SCR_WIDTH + WIDTH) / (tubeCount - 1);
    }

    // перед каждой игрой: картинки выбранной карты, скорость и просвет уровня, место на старте
    public void reset(Texture upperTexture, Texture downTexture, int speed, int gapHeight) {
        textureUpperTube = upperTexture;
        textureDownTube = downTexture;
        this.speed = speed;
        this.gapHeight = gapHeight; // сначала просвет, потом его место: место зависит от просвета
        x = distanceBetweenTubes * tubeIdx + MyGdxGame.SCR_WIDTH;
        newPair();
    }

    // колонна становится новой парой: новое место просвета, очко и замер ещё не получены
    private void newPair() {
        gapY = gapHeight / 2 + PADDING + random.nextInt(MyGdxGame.SCR_HEIGHT - 2 * (PADDING + gapHeight / 2));
        isPointReceived = false;
        closestDistance = MyGdxGame.SCR_HEIGHT;
    }

    public void move() {
        x -= speed;
        if (x < -WIDTH) {
            x = MyGdxGame.SCR_WIDTH + distanceBetweenTubes;
            newPair();
        }
    }

    private int getGapBottom() {
        return gapY - gapHeight / 2;
    }

    private int getGapTop() {
        return gapY + gapHeight / 2;
    }

    // по горизонтали птица там же, где колонна
    private boolean isOverTube(Bird bird) {
        return bird.getHitboxRight() >= x && bird.getHitboxLeft() <= x + WIDTH;
    }

    public boolean isHit(Bird bird) {
        boolean isOutOfGap = bird.getHitboxBottom() <= getGapBottom() || bird.getHitboxTop() >= getGapTop();
        return isOverTube(bird) && isOutOfGap;
    }

    // пока птица над колонной, запоминаем самое маленькое расстояние до края просвета
    public void updateClosestDistance(Bird bird) {
        if (isOverTube(bird)) {
            int toLowerTube = bird.getHitboxBottom() - getGapBottom();
            int toUpperTube = getGapTop() - bird.getHitboxTop();
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

    public boolean needAddPoint(Bird bird) {
        return !isPointReceived && bird.getX() > x + WIDTH;
    }

    public void setPointReceived() {
        isPointReceived = true;
    }

    public void draw(Batch batch) {
        batch.draw(textureUpperTube, x, getGapTop(), WIDTH, HEIGHT);
        batch.draw(textureDownTube, x, getGapBottom() - HEIGHT, WIDTH, HEIGHT);
    }
}
