package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.ImageButton;
import ru.samsung.gamestudio.components.TitleText;

public class ScreenMap implements Screen {
    private static final int BUTTONS_Y = 250;
    private static final int BUTTON_HEIGHT = 270;
    private static final int[] BUTTON_CENTER_X = {340, 940}; // по одному числу на каждую карту

    private MyGdxGame myGdxGame;
    private TitleText title;
    private ImageButton[] mapButtons; // номер кнопки = номер карты

    public ScreenMap(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        title = new TitleText("Choose map");

        mapButtons = new ImageButton[MyGdxGame.MAP_NAMES.length];
        for (int i = 0; i < mapButtons.length; i++) {
            mapButtons[i] = new ImageButton(MyGdxGame.MAP_BACKGROUNDS[i], MyGdxGame.MAP_NAMES[i],
                BUTTON_CENTER_X[i], BUTTONS_Y, BUTTON_HEIGHT);
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = myGdxGame.getTouch();
            int touchX = (int) touch.x;
            int touchY = (int) touch.y;

            for (int i = 0; i < mapButtons.length; i++) {
                if (mapButtons[i].isHit(touchX, touchY)) {
                    myGdxGame.sounds.playClick();
                    myGdxGame.screenGame.setMap(i);
                    myGdxGame.setScreen(myGdxGame.screenSkin);
                }
            }
        }

        myGdxGame.menuBackground.move();

        myGdxGame.beginDraw();
        myGdxGame.menuBackground.draw(myGdxGame.batch);
        title.draw(myGdxGame.batch);
        for (ImageButton button : mapButtons) {
            button.draw(myGdxGame.batch);
        }
        myGdxGame.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        title.dispose();
        for (ImageButton button : mapButtons) {
            button.dispose();
        }
    }
}
