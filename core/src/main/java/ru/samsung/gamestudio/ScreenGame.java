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

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        bird = new Bird(100, 300, 5);
        initTubes();
    }

    private void initTubes() {
        tubes = new Tube[tubeCount];
        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i);
        }
    }

    @Override
    public void show() {
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
                }
            }
        }

        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();

        for (Tube tube : tubes) {
            tube.draw(myGdxGame.batch);
        }

        bird.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        bird.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }
    }
}
