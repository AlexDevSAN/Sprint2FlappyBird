package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import ru.samsung.gamestudio.Difficulty;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.TextButton;
import ru.samsung.gamestudio.components.TitleText;

public class ScreenLevel implements Screen {
    private static final int BUTTON_WIDTH = 470; // ширина картинки button_bg.png
    private static final int BUTTON_X = (MyGdxGame.SCR_WIDTH - BUTTON_WIDTH) / 2; // кнопки по центру
    private static final int[] BUTTON_Y = {400, 215, 30}; // сверху вниз: Easy, Medium, Hard

    private MyGdxGame myGdxGame;
    private TitleText title;
    private TextButton[] levelButtons; // номер кнопки = номер уровня

    public ScreenLevel(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        title = new TitleText("Choose level");

        levelButtons = new TextButton[Difficulty.LEVEL_NAMES.length];
        for (int i = 0; i < levelButtons.length; i++) {
            levelButtons[i] = new TextButton(BUTTON_X, BUTTON_Y[i], Difficulty.LEVEL_NAMES[i]);
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

            for (int i = 0; i < levelButtons.length; i++) {
                if (levelButtons[i].isHit(touchX, touchY)) {
                    myGdxGame.sounds.playClick();
                    myGdxGame.difficulty.setLevel(i);
                    // show() вызовется сам внутри setScreen, вручную не надо
                    myGdxGame.setScreen(myGdxGame.screenGame);
                }
            }
        }

        myGdxGame.menuBackground.move();

        myGdxGame.beginDraw();
        myGdxGame.menuBackground.draw(myGdxGame.batch);
        title.draw(myGdxGame.batch);
        for (TextButton button : levelButtons) {
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
        for (TextButton button : levelButtons) {
            button.dispose();
        }
    }
}
