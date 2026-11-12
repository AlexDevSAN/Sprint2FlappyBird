package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.character.Bird;
import ru.samsung.gamestudio.character.Tube;
import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.components.ParticleSystem;
import ru.samsung.gamestudio.components.PointCounter;

public class ScreenGame implements Screen {
    private static final int TUBE_COUNT = 4;
    private static final int BIRD_X = 100;
    private static final int BIRD_SPEED = 5;
    private static final int POINT_COUNTER_MARGIN_TOP = 60;
    private static final int POINT_COUNTER_MARGIN_RIGHT = 400;

    private MyGdxGame myGdxGame;

    private Bird[] birds; // все птицы, номер = номер птицы
    private Bird bird; // птица, которой играем сейчас

    private MovingBackground[] backgrounds; // фоны карт, номер = номер карты
    private MovingBackground background; // фон выбранной карты
    // картинки колонн по номеру карты: грузим один раз, колонны только берут их
    private Texture[] upperTubeTextures;
    private Texture[] downTubeTextures;
    private Tube[] tubes;
    private int mapIndex;

    private ParticleSystem particles; // дым, перья или искры за птицей
    private PointCounter pointCounter;
    private int gamePoints;
    private boolean isGameOver;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        // всё грузим один раз, потом только выбираем нужное
        birds = new Bird[MyGdxGame.BIRD_FOLDERS.length];
        for (int i = 0; i < birds.length; i++) {
            birds[i] = new Bird(BIRD_X, MyGdxGame.SCR_HEIGHT / 2, BIRD_SPEED, MyGdxGame.BIRD_FOLDERS[i]);
        }

        int mapCount = MyGdxGame.MAP_NAMES.length;
        backgrounds = new MovingBackground[mapCount];
        upperTubeTextures = new Texture[mapCount];
        downTubeTextures = new Texture[mapCount];
        for (int i = 0; i < mapCount; i++) {
            backgrounds[i] = new MovingBackground(MyGdxGame.MAP_BACKGROUNDS[i]);
            upperTubeTextures[i] = new Texture(MyGdxGame.MAP_UPPER_TUBES[i]);
            downTubeTextures[i] = new Texture(MyGdxGame.MAP_DOWN_TUBES[i]);
        }

        // колонны тоже создаём один раз, перед каждой игрой их сбрасывает show()
        tubes = new Tube[TUBE_COUNT];
        for (int i = 0; i < TUBE_COUNT; i++) {
            tubes[i] = new Tube(TUBE_COUNT, i);
        }

        particles = new ParticleSystem(); // до setBird: там частицам сразу сообщают номер птицы
        pointCounter = new PointCounter(MyGdxGame.SCR_WIDTH - POINT_COUNTER_MARGIN_RIGHT, MyGdxGame.SCR_HEIGHT - POINT_COUNTER_MARGIN_TOP);

        setBird(0);
        setMap(MyGdxGame.MAP_CITY);
    }

    // эти два метода вызывают экраны выбора птицы и карты
    public void setBird(int birdIndex) {
        bird = birds[birdIndex];
        particles.setSkin(birdIndex); // шлейф как у выбранной птицы
    }

    public void setMap(int mapIndex) {
        this.mapIndex = mapIndex;
        background = backgrounds[mapIndex];
    }

    // setScreen вызывает show() перед каждой новой игрой: возвращаем всё к началу
    @Override
    public void show() {
        gamePoints = 0;
        isGameOver = false;
        bird.reset();

        // колонны получают картинки карты и числа уровня в начале каждой игры
        for (Tube tube : tubes) {
            tube.reset(upperTubeTextures[mapIndex], downTubeTextures[mapIndex],
                myGdxGame.difficulty.getTubeSpeed(), myGdxGame.difficulty.getGapHeight());
        }
        background.setSpeed(myGdxGame.difficulty.getBackgroundSpeed());
        particles.setWorldSpeed(myGdxGame.difficulty.getTubeSpeed()); // частицы уносит со скоростью колонн
        particles.clear(); // старые частицы из прошлой игры не нужны

        myGdxGame.achievementTracker.reset(); // достижения считаются за одну игру
        myGdxGame.sounds.playMapMusic(mapIndex); // у каждой карты своя музыка
    }

    @Override
    public void render(float delta) {
        if (!isGameOver) {
            update();
        }
        draw();
        if (isGameOver) {
            gameOver();
        }
    }

    // один шаг игры: тап, движение, столкновения и очки
    private void update() {
        if (Gdx.input.justTouched()) {
            bird.onClick();
            particles.addBurst(bird);
            myGdxGame.sounds.playFlap();
        }

        background.move();
        bird.fly();
        particles.addTrail(bird);
        particles.move();

        if (!bird.isInField()) {
            isGameOver = true;
        }

        for (Tube tube : tubes) {
            tube.move();
            tube.updateClosestDistance(bird);

            if (tube.isHit(bird)) {
                isGameOver = true;
            } else if (tube.needAddPoint(bird)) {
                addPoint(tube);
            }
        }
    }

    private void addPoint(Tube tube) {
        gamePoints += 1;
        tube.setPointReceived();
        myGdxGame.sounds.playPoint();
        myGdxGame.achievementTracker.onPoint(gamePoints, mapIndex);
        myGdxGame.achievementTracker.onTubePassed(tube.getClosestDistance());
    }

    private void draw() {
        myGdxGame.beginDraw();
        background.draw(myGdxGame.batch);
        particles.draw(myGdxGame.batch); // до птицы, чтобы частицы были позади неё
        bird.draw(myGdxGame.batch);
        for (Tube tube : tubes) {
            tube.draw(myGdxGame.batch);
        }
        pointCounter.draw(myGdxGame.batch, gamePoints);
        myGdxGame.achievementTracker.drawPopup(myGdxGame.batch);
        myGdxGame.batch.end();

        if (myGdxGame.achievementTracker.isNewAchievement()) {
            myGdxGame.sounds.playAchievement(); // мелодия, когда всплывает новая плашка
        }
    }

    // проигрыш: удар звучит один раз, даже если в одном кадре врезались и вылетели
    private void gameOver() {
        myGdxGame.sounds.playHit();
        myGdxGame.achievementTracker.onGameOver(gamePoints);
        myGdxGame.screenRestart.setGamePoints(gamePoints);
        myGdxGame.setScreen(myGdxGame.screenRestart);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        for (Bird oneBird : birds) {
            oneBird.dispose();
        }
        for (MovingBackground mapBackground : backgrounds) {
            mapBackground.dispose();
        }
        for (Texture texture : upperTubeTextures) {
            texture.dispose();
        }
        for (Texture texture : downTubeTextures) {
            texture.dispose();
        }
        particles.dispose();
        pointCounter.dispose();
    }
}
