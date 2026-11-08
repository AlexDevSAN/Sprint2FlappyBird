package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.ImageButton;
import ru.samsung.gamestudio.components.MovingBackground;

public class ScreenMap implements Screen {
    MyGdxGame myGdxGame;

    MovingBackground background;
    ImageButton[] mapButtons;

    BitmapFont titleFont;
    String title = "Choose map";
    int titleX;
    final int titleY = 650;

    public ScreenMap(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        background = new MovingBackground("background/restart_bg.png"); // move() не вызываем, фон стоит на месте

        // номер кнопки = номер карты в ScreenGame (0 - город, 1 - пещера)
        mapButtons = new ImageButton[]{
            new ImageButton("background/game_bg.png", "City", 340, 250, 270),
            new ImageButton("cave/cave_bg.png", "Cave", 940, 250, 270)
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

            for (int i = 0; i < mapButtons.length; i++) {
                if (mapButtons[i].isHit(touchX, touchY)) {
                    myGdxGame.sounds.playClick();
                    myGdxGame.screenGame.setMap(i);
                    myGdxGame.setScreen(myGdxGame.screenSkin);
                }
            }
        }

        ScreenUtils.clear(0, 0, 0, 1);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        titleFont.draw(myGdxGame.batch, title, titleX, titleY);
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
        background.dispose();
        titleFont.dispose();
        for (ImageButton button : mapButtons) {
            button.dispose();
        }
    }
}
