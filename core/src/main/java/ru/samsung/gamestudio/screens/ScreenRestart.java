package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.PointCounter;
import ru.samsung.gamestudio.components.TextButton;

public class ScreenRestart implements Screen {
    private static final int BUTTON_X = 100;
    private static final int RESTART_BUTTON_Y = 450;
    private static final int MENU_BUTTON_Y = 250;
    private static final int POINT_COUNTER_X = 750;
    private static final int POINT_COUNTER_Y = 530;

    private MyGdxGame myGdxGame;
    private TextButton buttonRestart;
    private TextButton buttonMenu;
    private PointCounter pointCounter;
    private int gamePoints;

    public ScreenRestart(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        buttonRestart = new TextButton(BUTTON_X, RESTART_BUTTON_Y, "Restart");
        buttonMenu = new TextButton(BUTTON_X, MENU_BUTTON_Y, "Main Menu");
        pointCounter = new PointCounter(POINT_COUNTER_X, POINT_COUNTER_Y);
    }

    // счёт сюда передаёт ScreenGame перед тем, как открыть этот экран
    public void setGamePoints(int gamePoints) {
        this.gamePoints = gamePoints;
    }

    @Override
    public void show() {
        myGdxGame.sounds.stopMusic(); // на этом экране музыки нет, только джингл
        myGdxGame.sounds.playGameOver();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = myGdxGame.getTouch();
            int touchX = (int) touch.x;
            int touchY = (int) touch.y;

            if (buttonRestart.isHit(touchX, touchY)) {
                myGdxGame.sounds.playClick();
                // show() вызовется сам внутри setScreen, вручную не надо
                myGdxGame.setScreen(myGdxGame.screenGame);
            } else if (buttonMenu.isHit(touchX, touchY)) {
                myGdxGame.sounds.playClick();
                myGdxGame.setScreen(myGdxGame.screenMenu);
            }
        }

        myGdxGame.menuBackground.move();

        myGdxGame.beginDraw();
        myGdxGame.menuBackground.draw(myGdxGame.batch);
        buttonRestart.draw(myGdxGame.batch);
        buttonMenu.draw(myGdxGame.batch);
        pointCounter.draw(myGdxGame.batch, gamePoints);
        myGdxGame.achievementTracker.drawList(myGdxGame.batch);
        myGdxGame.achievementTracker.drawPopup(myGdxGame.batch); // Kamikaze и недопоказанные плашки
        myGdxGame.batch.end();

        if (myGdxGame.achievementTracker.isNewAchievement()) {
            myGdxGame.sounds.playAchievement();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        buttonRestart.dispose();
        buttonMenu.dispose();
        pointCounter.dispose();
    }
}
