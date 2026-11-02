package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.components.PointCounter;
import ru.samsung.gamestudio.components.TextButton;

public class ScreenRestart implements Screen {
    MyGdxGame myGdxGame;
    MovingBackground background;
    TextButton buttonRestart;

    PointCounter pointCounter;
    public int gamePoints;

    public ScreenRestart(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        buttonRestart = new TextButton(100, 400, "Restart");
        background = new MovingBackground("background/restart_bg.png");
        pointCounter = new PointCounter(750, 530);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // ИЗМЕНЕНИЕ: Полностью исправлен и дописан блок отслеживания кликов по кнопке Restart
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            myGdxGame.camera.unproject(touch);

            int touchX = (int) touch.x;
            int touchY = (int) touch.y;

            if (buttonRestart.isHit(touchX, touchY)) {
                myGdxGame.screenGame.show();
                myGdxGame.setScreen(myGdxGame.screenGame);
            }
        }

        background.move();

        ScreenUtils.clear(1, 0, 0, 1);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        buttonRestart.draw(myGdxGame.batch);
        pointCounter.draw(myGdxGame.batch, gamePoints);
        myGdxGame.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        buttonRestart.dispose();
        pointCounter.dispose();
    }
}
