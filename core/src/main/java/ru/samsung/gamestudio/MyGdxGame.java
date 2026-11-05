package ru.samsung.gamestudio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.screens.ScreenGame;
import ru.samsung.gamestudio.screens.ScreenRestart;
import ru.samsung.gamestudio.screens.screenMenu;

public class MyGdxGame extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public ScreenGame screenGame;
    public ScreenRestart screenRestart;

    public screenMenu screenMenu;

    public static final int SCR_WIDTH = 1280;
    public static final int SCR_HEIGHT = 720;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);

        screenGame = new ScreenGame(this);
        screenRestart = new ScreenRestart(this);
        // ИЗМЕНЕНИЕ: Создаем объект класса screenMenu с маленькой буквы
        screenMenu = new screenMenu(this);

        setScreen(screenMenu);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
