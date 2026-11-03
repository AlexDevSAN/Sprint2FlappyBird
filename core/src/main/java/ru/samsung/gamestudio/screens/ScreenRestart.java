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
    // ИЗМЕНЕНИЕ: Добавлено поле для кнопки выхода в меню
    TextButton buttonMenu;

    PointCounter pointCounter;
    public int gamePoints;

    public ScreenRestart(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        buttonRestart = new TextButton(100, 450, "Restart");
        // ИЗМЕНЕНИЕ: Инициализация кнопки меню на экране рестарта
        buttonMenu = new TextButton(100, 250, "Main Menu");
        background = new MovingBackground("background/restart_bg.png");
        pointCounter = new PointCounter(750, 530);
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

            if (buttonRestart.isHit(touchX, touchY)) {
                myGdxGame.screenGame.show();
                myGdxGame.setScreen(myGdxGame.screenGame);
            } else if (buttonMenu.isHit(touchX, touchY)) {
                // ИЗМЕНЕНИЕ: Переключение на экран главного меню при клике на buttonMenu
                myGdxGame.setScreen(myGdxGame.screenMenu);
            }
        }

        background.move();

        ScreenUtils.clear(1, 0, 0, 1);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        buttonRestart.draw(myGdxGame.batch);
        // ИЗМЕНЕНИЕ: Отрисовка кнопки меню
        buttonMenu.draw(myGdxGame.batch);
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
        buttonMenu.dispose(); // ИЗМЕНЕНИЕ: Очистка ресурсов новой кнопки
        pointCounter.dispose();
    }
}
