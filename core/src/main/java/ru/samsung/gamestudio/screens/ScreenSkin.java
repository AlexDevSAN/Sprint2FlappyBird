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

public class ScreenSkin implements Screen {
    MyGdxGame myGdxGame;

    MovingBackground background;
    ImageButton[] skinButtons;

    BitmapFont titleFont;
    String title = "Choose bird";
    int titleX;
    final int titleY = 650;

    public ScreenSkin(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        background = new MovingBackground("background/restart_bg.png"); // move() не вызываем, фон стоит на месте

        // номер кнопки = номер птицы в ScreenGame (0 - синяя, 1 - классика, 2 - герой)
        skinButtons = new ImageButton[]{
            new ImageButton("birdTiles/bird0.png", "Blue", 240, 260, 220),
            new ImageButton("birdClassic/bird0.png", "Classic", 640, 260, 220),
            new ImageButton("birdHero/bird0.png", "Hero", 1040, 260, 220)
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

            for (int i = 0; i < skinButtons.length; i++) {
                if (skinButtons[i].isHit(touchX, touchY)) {
                    myGdxGame.sounds.playClick();
                    myGdxGame.screenGame.setBird(i);
                    // после птицы выбираем уровень, игра запустится оттуда
                    myGdxGame.setScreen(myGdxGame.screenLevel);
                }
            }
        }

        ScreenUtils.clear(0, 0, 0, 1);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        titleFont.draw(myGdxGame.batch, title, titleX, titleY);
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
        background.dispose();
        titleFont.dispose();
        for (ImageButton button : skinButtons) {
            button.dispose();
        }
    }
}
