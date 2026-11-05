package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.character.Bird;
import ru.samsung.gamestudio.character.Tube;
import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.components.PointCounter;

public class ScreenGame implements Screen {
    MyGdxGame myGdxGame;
    Bird bird;

    Tube[] tubes;
    int tubeCount = 4;
    public boolean isGameOver;
    int gamePoints;

    PointCounter pointCounter;
    MovingBackground background;

    final int pointCounterMarginTop = 60;
    final int pointCounterMarginRight = 400;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        bird = new Bird(100, 300, 5);
        initTubes();

        pointCounter = new PointCounter(MyGdxGame.SCR_WIDTH - pointCounterMarginRight, MyGdxGame.SCR_HEIGHT - pointCounterMarginTop);
        background = new MovingBackground("background/game_bg.png");
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
        bird.y = MyGdxGame.SCR_HEIGHT / 2;
        initTubes();
    }

    @Override
    public void render(float delta) {
        if (!isGameOver && Gdx.input.justTouched()) {
            bird.onClick();
        }

        if (!isGameOver) {
            background.move();
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

        ScreenUtils.clear(1, 0, 0, 1);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);
        bird.draw(myGdxGame.batch);

        for (Tube tube : tubes) {
            tube.draw(myGdxGame.batch);
        }

        pointCounter.draw(myGdxGame.batch, gamePoints);

        myGdxGame.batch.end();

        if (isGameOver) {
            myGdxGame.screenRestart.gamePoints = gamePoints;
            myGdxGame.setScreen(myGdxGame.screenRestart);
        }
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
        background.dispose();
    }
}
