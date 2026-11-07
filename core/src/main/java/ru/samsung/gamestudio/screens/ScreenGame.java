package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.character.Bird;
import ru.samsung.gamestudio.character.Tube;
import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.components.PointCounter;

public class ScreenGame implements Screen {
    MyGdxGame myGdxGame;
    Bird bird; // птица, которой играем сейчас
    Bird[] birds; // все птицы: 0 - синяя, 1 - классика, 2 - герой

    Tube[] tubes;
    int tubeCount = 4;
    public boolean isGameOver;
    int gamePoints;

    PointCounter pointCounter;
    MovingBackground background; // фон выбранной карты
    MovingBackground[] backgrounds; // фоны карт: 0 - город, 1 - пещера

    int mapIndex; // номер выбранной карты
    // картинки колонн для каждой карты, в пещере вместо труб камни
    final String[] upperTubePaths = {"Tube/tube_flipped.png", "cave/stone_flipped.png"};
    final String[] downTubePaths = {"Tube/tube.png", "cave/stone.png"};

    final int pointCounterMarginTop = 60;
    final int pointCounterMarginRight = 400;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        // всех птиц и все фоны грузим один раз, потом только выбираем нужные
        birds = new Bird[]{
            new Bird(100, 300, 5, "birdTiles"),
            new Bird(100, 300, 5, "birdClassic"),
            new Bird(100, 300, 5, "birdHero")
        };
        backgrounds = new MovingBackground[]{
            new MovingBackground("background/game_bg.png"),
            new MovingBackground("cave/cave_bg.png")
        };
        setBird(0);
        setMap(0);
        initTubes();

        pointCounter = new PointCounter(MyGdxGame.SCR_WIDTH - pointCounterMarginRight, MyGdxGame.SCR_HEIGHT - pointCounterMarginTop);
    }

    // эти два метода вызывают экраны выбора птицы и карты
    public void setBird(int birdIndex) {
        bird = birds[birdIndex];
    }

    public void setMap(int mapIndex) {
        this.mapIndex = mapIndex;
        background = backgrounds[mapIndex];
    }

    private void initTubes() {
        tubes = new Tube[tubeCount];
        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i, upperTubePaths[mapIndex], downTubePaths[mapIndex]);
        }
    }


    @Override
    public void show() {
        gamePoints = 0;
        isGameOver = false;
        bird.y = MyGdxGame.SCR_HEIGHT / 2;
        initTubes();
        // уровень ставим в начале каждой игры, а не только когда колонны создаются
        for (Tube tube : tubes) {
            tube.setDifficulty(myGdxGame.difficulty.getTubeSpeed(), myGdxGame.difficulty.getGapHeight());
        }
        background.setSpeed(myGdxGame.difficulty.getBackgroundSpeed());
    }

    @Override
    public void render(float delta) {
        if (!isGameOver && Gdx.input.justTouched()) {
            bird.onClick();
        }

        if (!isGameOver) {
            background.move();
            bird.fly();

            if (!bird.isInField()) {
                System.out.println("not in field");
                isGameOver = true;
            }

            for (Tube tube : tubes) {
                tube.move();

                if (tube.isHit(bird)) {
                    System.out.println("hit");
                    isGameOver = true;
                } else if (tube.needAddPoint(bird)) {
                    gamePoints += 1;
                    tube.setPointReceived();
                    System.out.println(gamePoints);
                }
            }
        }

        ScreenUtils.clear(1, 0, 0, 1);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);
        bird.draw(myGdxGame.batch);

        for (Tube tube : tubes) {
            tube.draw(myGdxGame.batch);
        }

        pointCounter.draw(myGdxGame.batch, gamePoints);

        myGdxGame.batch.end();

        if (isGameOver) {
            myGdxGame.screenRestart.gamePoints = gamePoints;
            myGdxGame.setScreen(myGdxGame.screenRestart);
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        for (Bird skin : birds) {
            skin.dispose();
        }
        for (Tube tube : tubes) {
            tube.dispose();
        }
        pointCounter.dispose();
        for (MovingBackground mapBackground : backgrounds) {
            mapBackground.dispose();
        }
    }
}
