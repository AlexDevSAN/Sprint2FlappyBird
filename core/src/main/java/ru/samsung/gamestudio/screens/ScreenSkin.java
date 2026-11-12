package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.ImageButton;
import ru.samsung.gamestudio.components.TitleText;

public class ScreenSkin implements Screen {
    private static final int BUTTONS_Y = 260;
    private static final int BUTTON_HEIGHT = 200; // четыре птицы в ряд, поэтому чуть меньше
    private static final int[] BUTTON_CENTER_X = {160, 480, 800, 1120}; // по одному числу на каждую птицу

    private MyGdxGame myGdxGame;
    private TitleText title;
    private ImageButton[] skinButtons; // номер кнопки = номер птицы

    public ScreenSkin(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        title = new TitleText("Choose bird");

        // на кнопке первый кадр птицы
        skinButtons = new ImageButton[MyGdxGame.BIRD_NAMES.length];
        for (int i = 0; i < skinButtons.length; i++) {
            skinButtons[i] = new ImageButton(MyGdxGame.BIRD_FOLDERS[i] + "/bird0.png", MyGdxGame.BIRD_NAMES[i],
                BUTTON_CENTER_X[i], BUTTONS_Y, BUTTON_HEIGHT);
        }
    }

    @Override
    public void show() {
        // секретная птица закрыта, пока её не откроет пасхалка
        skinButtons[MyGdxGame.BIRD_MAD].setLocked(!myGdxGame.isMadBirdUnlocked);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = myGdxGame.getTouch();
            int touchX = (int) touch.x;
            int touchY = (int) touch.y;

            for (int i = 0; i < skinButtons.length; i++) {
                if (skinButtons[i].isHit(touchX, touchY)) {
                    myGdxGame.sounds.playClick();
                    myGdxGame.screenGame.setBird(i);
                    myGdxGame.setScreen(myGdxGame.screenLevel);
                }
            }
        }

        myGdxGame.menuBackground.move();

        myGdxGame.beginDraw();
        myGdxGame.menuBackground.draw(myGdxGame.batch);
        title.draw(myGdxGame.batch);
        for (ImageButton button : skinButtons) {
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
        for (ImageButton button : skinButtons) {
            button.dispose();
        }
    }
}
