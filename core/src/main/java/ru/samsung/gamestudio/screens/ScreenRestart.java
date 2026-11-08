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
        myGdxGame.sounds.stopMusic(); // на этом экране музыки нет, только джингл
        myGdxGame.sounds.playGameOver();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            myGdxGame.camera.unproject(touch);

            int touchX = (int) touch.x;
            int touchY = (int) touch.y;

            if (buttonRestart.isHit(touchX, touchY)) {
                myGdxGame.sounds.playClick();
                myGdxGame.screenGame.show();
                myGdxGame.setScreen(myGdxGame.screenGame);
            } else if (buttonMenu.isHit(touchX, touchY)) {
                myGdxGame.sounds.playClick();

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
        myGdxGame.screenGame.achievementTracker.drawList(myGdxGame.batch);
        myGdxGame.screenGame.achievementTracker.drawPopup(myGdxGame.batch); // Kamikaze и недопоказанные плашки
        if (myGdxGame.screenGame.achievementTracker.isNewAchievement()) {
            myGdxGame.sounds.playAchievement(); // и здесь мелодия для новой плашки
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
        buttonRestart.dispose();
        buttonMenu.dispose(); // ИЗМЕНЕНИЕ: Очистка ресурсов новой кнопки
        pointCounter.dispose();
    }
}
