package ru.samsung.gamestudio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.components.AchievementTracker;
import ru.samsung.gamestudio.components.GameSounds;
import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.screens.ScreenEaster;
import ru.samsung.gamestudio.screens.ScreenGame;
import ru.samsung.gamestudio.screens.ScreenLevel;
import ru.samsung.gamestudio.screens.ScreenMap;
import ru.samsung.gamestudio.screens.ScreenMenu;
import ru.samsung.gamestudio.screens.ScreenRestart;
import ru.samsung.gamestudio.screens.ScreenSkin;

public class MyGdxGame extends Game {
    public static final int SCR_WIDTH = 1280;
    public static final int SCR_HEIGHT = 720;
    public static final int MENU_BACKGROUND_SPEED = 1; // в меню фон едет вдвое медленнее, чем в игре на Medium

    // карты: номер в массиве = номер карты, все списки в одном порядке
    public static final int MAP_CITY = 0;
    public static final int MAP_CAVE = 1;
    public static final String[] MAP_NAMES = {"City", "Cave"};
    public static final String[] MAP_BACKGROUNDS = {"background/game_bg.png", "cave/cave_bg.png"};
    public static final String[] MAP_UPPER_TUBES = {"Tube/tube_flipped.png", "cave/stone_flipped.png"};
    public static final String[] MAP_DOWN_TUBES = {"Tube/tube.png", "cave/stone.png"};
    public static final String[] MAP_MUSIC = {"music/city.ogg", "music/cave.ogg"};

    // птицы: номер в массиве = номер птицы; последняя - секретная, её открывает пасхалка
    public static final int BIRD_MAD = 3;
    public static final String[] BIRD_NAMES = {"Blue", "Classic", "Hero", "Mad"};
    public static final String[] BIRD_FOLDERS = {"birdTiles", "birdClassic", "birdHero", "birdMad"};

    public SpriteBatch batch;
    public OrthographicCamera camera;

    // общие объекты для всех экранов
    public Difficulty difficulty;
    public GameSounds sounds;
    public AchievementTracker achievementTracker;
    public MovingBackground menuBackground; // один фон на меню, экраны выбора и рестарт

    // за этот запуск игры: сколько раз проиграли, лучший счёт, открыта ли секретная птица
    public int lossCount;
    public int bestScore;
    public boolean isMadBirdUnlocked;

    public ScreenMenu screenMenu;
    public ScreenMap screenMap;
    public ScreenSkin screenSkin;
    public ScreenLevel screenLevel;
    public ScreenGame screenGame;
    public ScreenRestart screenRestart;
    public ScreenEaster screenEaster;

    private Vector3 touch; // точка касания, одна на всю игру

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();

        // общие объекты создаём раньше экранов: экраны пользуются ими сразу
        difficulty = new Difficulty();
        sounds = new GameSounds();
        achievementTracker = new AchievementTracker();
        menuBackground = new MovingBackground("background/restart_bg.png");
        menuBackground.setSpeed(MENU_BACKGROUND_SPEED);

        screenMenu = new ScreenMenu(this);
        screenMap = new ScreenMap(this);
        screenSkin = new ScreenSkin(this);
        screenLevel = new ScreenLevel(this);
        screenGame = new ScreenGame(this);
        screenRestart = new ScreenRestart(this);
        screenEaster = new ScreenEaster(this);

        setScreen(screenMenu);
    }

    // где коснулись экрана, в координатах игры 1280x720
    public Vector3 getTouch() {
        touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touch);
        return touch;
    }

    // начало кадра, одинаковое для всех экранов: очистить экран, обновить камеру, начать рисовать
    public void beginDraw() {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    public void dispose() {
        // каждый экран освобождает своё, а общие объекты освобождаем здесь, ровно один раз
        screenMenu.dispose();
        screenMap.dispose();
        screenSkin.dispose();
        screenLevel.dispose();
        screenGame.dispose();
        screenRestart.dispose();
        screenEaster.dispose();
        menuBackground.dispose();
        achievementTracker.dispose();
        sounds.dispose();
        batch.dispose();
    }
}
