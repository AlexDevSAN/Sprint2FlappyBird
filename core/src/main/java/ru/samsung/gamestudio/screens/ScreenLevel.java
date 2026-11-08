package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.components.TextButton;

public class ScreenLevel implements Screen {
    MyGdxGame myGdxGame;

    MovingBackground background;
    TextButton[] levelButtons;

    BitmapFont titleFont;
    String title = "Choose level";
    int titleX;
    final int titleY = 650;

    public ScreenLevel(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        background = new MovingBackground("background/restart_bg.png"); // move() не вызываем, фон стоит на месте

        // три кнопки столбиком по центру, 470 - ширина картинки button_bg.png
        int buttonX = (MyGdxGame.SCR_WIDTH - 470) / 2;
        // номер кнопки = номер уровня в Difficulty (0 - Easy, 1 - Medium, 2 - Hard)
        levelButtons = new TextButton[]{
            new TextButton(buttonX, 400, "Easy"),
            new TextButton(buttonX, 215, "Medium"),
            new TextButton(buttonX, 30, "Hard")
        };

        titleFont = new BitmapFont();
        titleFont.getData().setScale(4f);
        titleFont.setColor(Color.WHITE);
        // заголовок по центру: меряем его ширину
        GlyphLayout gl = new GlyphLayout(titleFont, title);
        titleX = (MyGdxGame.SCR_WIDTH - (int) gl.width) / 2;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            myGdxGame.camera.unproject(touch);

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

        ScreenUtils.clear(0, 0, 0, 1);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        titleFont.draw(myGdxGame.batch, title, titleX, titleY);
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
        background.dispose();
        titleFont.dispose();
        for (TextButton button : levelButtons) {
            button.dispose();
        }
    }
}
