package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.components.TextButton;


public class screenMenu implements Screen {
    MyGdxGame myGdxGame;

    MovingBackground background;
    TextButton buttonStart;
    TextButton buttonExit;

    public screenMenu(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        buttonStart = new TextButton(100, 450, "Start Game");
        buttonExit = new TextButton(100, 250, "Exit");
        background = new MovingBackground("background/restart_bg.png");
    }

    @Override
    public void show() {
        myGdxGame.sounds.playMenuMusic();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            myGdxGame.camera.unproject(touch);

            int touchX = (int) touch.x;
            int touchY = (int) touch.y;

            if (buttonStart.isHit(touchX, touchY)) {
                myGdxGame.sounds.playClick();
                // сначала выбираем карту, потом птицу, потом уже игра
                myGdxGame.setScreen(myGdxGame.screenMap);
            } else if (buttonExit.isHit(touchX, touchY)) {
                Gdx.app.exit();
            }
        }

        background.move();

        ScreenUtils.clear(0, 0, 0, 1);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        buttonStart.draw(myGdxGame.batch);
        buttonExit.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        buttonStart.dispose();
        buttonExit.dispose();
    }
}
