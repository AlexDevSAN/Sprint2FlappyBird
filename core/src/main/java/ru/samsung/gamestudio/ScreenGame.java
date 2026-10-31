package ru.samsung.gamestudio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenGame implements Screen {
    MyGdxGame myGdxGame;
    Bird bird;

    Tube[] tubes;
    int tubeCount = 4;
    boolean isGameOver;
    int gamePoints;

    PointCounter pointCounter;

    final int pointCounterMarginTop = 60;
    final int pointCounterMarginRight = 400;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        bird = new Bird(100, 300, 5);
        initTubes();

        pointCounter = new PointCounter(MyGdxGame.SCR_WIDTH - pointCounterMarginRight, MyGdxGame.SCR_HEIGHT - pointCounterMarginTop);
    }

    private void initTubes() {
        tubes = new Tube[tubeCount];
        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i);
        }
    }

    @Override
    public void show() {
        gamePoints = 0;
        isGameOver = false;
    }

    @Override
    public void render(float delta) {
        if (!isGameOver && Gdx.input.justTouched()) {
            bird.onClick();
        }

        if (!isGameOver) {
            bird.fly();

            if (!bird.isInField()) {
                System.out.println("not in field");
                isGameOver = true;
            }

            for (Tube tube : tubes) {
                tube.move();

                if (tube.isHit(bird)) {
                    System.out.println("hit");
                    isGameOver = true;
                } else if (tube.needAddPoint(bird)) {
                    gamePoints += 1;
                    tube.setPointReceived();
                    System.out.println(gamePoints);
                }
            }
        }

        // Очистка экрана красным цветом (1, 0, 0, 1)
        ScreenUtils.clear(1, 0, 0, 1);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();

        for (Tube tube : tubes) {
            tube.draw(myGdxGame.batch);
        }

        bird.draw(myGdxGame.batch);

        pointCounter.draw(myGdxGame.batch, gamePoints);

        myGdxGame.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        bird.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }
        pointCounter.dispose();
    }
}
