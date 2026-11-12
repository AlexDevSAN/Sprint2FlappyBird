package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.TextButton;

public class ScreenMenu implements Screen {
    private static final int BUTTON_X = 100;
    private static final int START_BUTTON_Y = 450;
    private static final int EXIT_BUTTON_Y = 250;

    private MyGdxGame myGdxGame;
    private TextButton buttonStart;
    private TextButton buttonExit;

    public ScreenMenu(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        buttonStart = new TextButton(BUTTON_X, START_BUTTON_Y, "Start Game");
        buttonExit = new TextButton(BUTTON_X, EXIT_BUTTON_Y, "Exit");
    }

    @Override
    public void show() {
        myGdxGame.sounds.playMenuMusic();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = myGdxGame.getTouch();
            int touchX = (int) touch.x;
            int touchY = (int) touch.y;

            if (buttonStart.isHit(touchX, touchY)) {
                myGdxGame.sounds.playClick();
                // сначала выбираем карту, птицу и уровень, потом уже игра
                myGdxGame.setScreen(myGdxGame.screenMap);
            } else if (buttonExit.isHit(touchX, touchY)) {
                Gdx.app.exit();
            }
        }

        myGdxGame.menuBackground.move();

        myGdxGame.beginDraw();
        myGdxGame.menuBackground.draw(myGdxGame.batch);
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
        buttonStart.dispose();
        buttonExit.dispose();
    }
}
