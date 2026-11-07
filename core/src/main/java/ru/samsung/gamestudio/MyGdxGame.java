package ru.samsung.gamestudio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.samsung.gamestudio.screens.ScreenGame;
import ru.samsung.gamestudio.screens.ScreenLevel;
import ru.samsung.gamestudio.screens.ScreenMap;
import ru.samsung.gamestudio.screens.ScreenRestart;
import ru.samsung.gamestudio.screens.ScreenSkin;
import ru.samsung.gamestudio.screens.screenMenu;

public class MyGdxGame extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public ScreenGame screenGame;
    public ScreenRestart screenRestart;
    public ScreenMap screenMap; // выбор карты
    public ScreenSkin screenSkin; // выбор птицы
    public ScreenLevel screenLevel; // выбор уровня сложности
    public Difficulty difficulty; // текущий уровень и его числа, один на всю игру

    public screenMenu screenMenu;

    public static final int SCR_WIDTH = 1280;
    public static final int SCR_HEIGHT = 720;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);

        difficulty = new Difficulty(); // раньше экранов, экраны берут из него числа
        screenGame = new ScreenGame(this);
        screenRestart = new ScreenRestart(this);
        // ИЗМЕНЕНИЕ: Создаем объект класса screenMenu с маленькой буквы
        screenMenu = new screenMenu(this);
        screenMap = new ScreenMap(this);
        screenSkin = new ScreenSkin(this);
        screenLevel = new ScreenLevel(this);

        setScreen(screenMenu);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
