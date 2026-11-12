package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.character.MadPortrait;
import ru.samsung.gamestudio.components.Particle;
import ru.samsung.gamestudio.components.Typewriter;
import java.util.Random;

// пасхалка "Definition of Insanity". На пятом проигрыше за запуск вместо рестарта
// сначала ломается экран рестарта, а потом безумная птица говорит с игроком его же игрой
public class ScreenEaster implements Screen {
    public static final int LOSSES_TO_SHOW = 5; // на каком проигрыше показываем сцену

    // шаги сцены, идут по порядку
    private static final int STEP_GLITCH = 0; // ложный рестарт ломается
    private static final int STEP_DARK = 1; // темнота, сердце, проступает логово
    private static final int STEP_ENTER = 2; // выезжает портрет
    private static final int STEP_TALK = 3; // печатается реплика
    private static final int STEP_LOOP = 4; // "Тап. Взмах. Труба. Смерть." всё быстрее
    private static final int STEP_WAIT_TAP = 5; // ждём, когда игрок тапнет
    private static final int STEP_LAUGH = 6; // хохот
    private static final int STEP_REWARD = 7; // награда

    // время в кадрах: 60 кадров - это 1 секунда
    private static final int GLITCH_FRAMES = 140;
    private static final int[] GLITCH_LEVEL_FRAMES = {45, 75, 105}; // в эти кадры надпись ломается сильнее
    private static final int FLASH_FRAMES = 10; // красная вспышка
    private static final float FLASH_ALPHA = 0.5f;
    private static final int DARK_FRAMES = 110;
    private static final int SECOND_HEARTBEAT_FRAME = 50;
    private static final int LAIR_FADE_START = 60; // с этого кадра темноты проступает логово
    private static final int HOLD_FRAMES = 75; // реплика допечатана и висит ещё немного
    private static final int[] BEAT_FRAMES = {34, 20, 11}; // три круга повтора, каждый быстрее
    private static final int LOOP_FINAL_FRAMES = 90;
    private static final int LAUGH_FRAMES = 60;
    private static final int LAUGH_PERIOD = 6; // "ха" раз в 6 кадров
    private static final int HINT_FRAME = 90; // через полторы секунды награды появляется подсказка
    private static final int REWARD_FRAMES = 420; // через 7 секунд уходим на рестарт сами

    // тряска: на сколько пикселей всё может сдвинуться
    private static final int GLITCH_SHAKE_STEP = 4;
    private static final int SHOUT_SHAKE = 4;
    private static final int DEATH_SHAKE = 10;
    private static final int FINAL_SHAKE = 14;
    private static final int LAUGH_SHAKE = 5;
    private static final int MARGIN = 16; // логово шире экрана на 16 пикселей с каждой стороны, при тряске краёв не видно

    // звук букв: высота немного разная, так речь живее
    private static final float BLIP_PITCH_MIN = 0.9f;
    private static final float BLIP_PITCH_RANGE = 0.2f;
    private static final float LAUGH_START_PITCH = 1.6f;
    private static final float LAUGH_PITCH_STEP = 0.08f; // каждое следующее "ха" чуть ниже

    // реплики: лицо и крик для каждой по номеру, сам текст собирает show()
    private static final int LINE_COUNT = 10;
    private static final int LINE_BEFORE_LOOP = 4; // после этой реплики идёт повтор смерти
    private static final int LINE_BEFORE_TAP = 6; // после этой ждём тап
    private static final int LINE_BEFORE_LAUGH = 7; // после этой хохот
    private static final int[] LINE_FACES = {
        MadPortrait.FACE_SMIRK, MadPortrait.FACE_SMIRK, MadPortrait.FACE_SHOUT, MadPortrait.FACE_SHOUT,
        MadPortrait.FACE_SMIRK, MadPortrait.FACE_SHOUT, MadPortrait.FACE_POINT, MadPortrait.FACE_POINT,
        MadPortrait.FACE_CALM, MadPortrait.FACE_CALM
    };
    private static final boolean[] LINE_SHOUTS = {false, false, true, true, false, true, false, false, false, false};
    private static final String[] NUMBER_WORDS = {"НОЛЬ", "ОДИН", "ДВА", "ТРИ", "ЧЕТЫРЕ", "ПЯТЬ", "ШЕСТЬ", "СЕМЬ",
        "ВОСЕМЬ", "ДЕВЯТЬ", "ДЕСЯТЬ", "ОДИННАДЦАТЬ", "ДВЕНАДЦАТЬ", "ТРИНАДЦАТЬ", "ЧЕТЫРНАДЦАТЬ", "ПЯТНАДЦАТЬ",
        "ШЕСТНАДЦАТЬ", "СЕМНАДЦАТЬ", "ВОСЕМНАДЦАТЬ", "ДЕВЯТНАДЦАТЬ"};
    private static final String[] TENS_WORDS = {"", "", "ДВАДЦАТЬ", "ТРИДЦАТЬ", "СОРОК", "ПЯТЬДЕСЯТ", "ШЕСТЬДЕСЯТ",
        "СЕМЬДЕСЯТ", "ВОСЕМЬДЕСЯТ", "ДЕВЯНОСТО"};
    private static final int LOOP_WORD_COUNT = 4; // Тап, Взмах, Труба, Смерть
    private static final int TUBE_WORD = 2; // с третьего слова появляется колонна
    private static final String HINT_TEXT = "Тапни, чтобы продолжить";

    // ложный рестарт: всё на тех же местах, что на настоящем
    private static final int BUTTON_X = 100;
    private static final int RESTART_BUTTON_Y = 450;
    private static final int MENU_BUTTON_Y = 250;
    private static final int SCORE_X = 750;
    private static final int SCORE_Y = 530;
    private static final String[] RESTART_LABELS = {"Restart", "R3$T4RT", "ЕЩЁ РАЗ?", "БЕЗУМИЕ"};
    private static final int FIRST_RUSSIAN_LABEL = 2; // русские надписи пишем шрифтом с кириллицей
    private static final String MENU_LABEL = "Main Menu";

    // повтор смерти игрока: где птица на каждом слове. Посередине экрана, чтобы не закрыть доску с засечками
    private static final int[] REPLAY_BIRD_X = {360, 440, 520, 580};
    private static final int[] REPLAY_BIRD_Y = {330, 400, 420, 440};
    private static final int REPLAY_BIRD_WIDTH = 170;
    private static final int REPLAY_TUBE_X = 680;
    private static final int REPLAY_TUBE_Y = 430;
    private static final int REPLAY_TUBE_WIDTH = 110;
    private static final int REPLAY_TUBE_HEIGHT = 385;
    private static final String CROSS_TEXT = "X";

    // титры внизу экрана
    private static final int SUBTITLE_BOX_HEIGHT = 160;
    private static final float SUBTITLE_BOX_ALPHA = 0.65f;
    private static final int SUBTITLE_X = 40;
    private static final int SUBTITLE_Y = 125;
    private static final Color SHOUT_COLOR = new Color(1f, 0.35f, 0.3f, 1f);

    // награда
    private static final int FEATHER_COUNT = 60;
    private static final int FEATHER_X = 1010; // перья вылетают из портрета
    private static final int FEATHER_Y = 260;
    private static final float FEATHER_SPEED_X = 8; // скорость по x от -8 до 8
    private static final float FEATHER_SPEED_Y_MIN = -2; // по y от -2 до 9, больше вверх
    private static final float FEATHER_SPEED_Y_RANGE = 11;
    private static final int FEATHER_SIZE = 44;
    private static final int FEATHER_LIFE = 90;
    // яркие, чтобы были видны в тёмном логове
    private static final Color[] FEATHER_COLORS = {new Color(1f, 0.2f, 0.2f, 1f), new Color(1f, 0.45f, 0.15f, 1f)};
    private static final String REWARD_TEXT = "Открыта секретная птица:\nMAD";
    private static final int REWARD_X = 60;
    private static final int REWARD_Y = 660;
    private static final int REWARD_BIRD_X = 420;
    private static final int REWARD_BIRD_Y = 300;
    private static final int REWARD_BIRD_WIDTH = 260;

    private MyGdxGame myGdxGame;
    private Random random;

    private Texture lairTexture;
    private Texture whiteTexture; // белый квадрат: красим в чёрный для титров и в красный для вспышки
    private Texture buttonTexture;
    private Texture featherTexture;
    private Texture[] birdTextures; // первый кадр каждой птицы, номер = номер птицы
    private Texture[] tubeTextures; // верхняя колонна каждой карты, номер = номер карты
    private BitmapFont defaultFont; // обычный шрифт, как на настоящем рестарте
    private BitmapFont font; // шрифт с русскими буквами для титров
    private BitmapFont bigFont; // он же, только крупнее
    private int[] labelX, labelY; // где писать каждую надпись кнопки, чтобы она была по центру
    private int menuLabelX, menuLabelY;
    private int crossHalfWidth, crossHalfHeight;

    private MadPortrait portrait;
    private Typewriter typewriter;
    private Particle[] feathers;

    // всё, что меняется за сцену
    private int step;
    private int stepFrame; // сколько кадров идёт текущий шаг
    private int glitchLevel; // 0 - надпись целая, 3 - совсем сломана
    private int flashFrames;
    private int shake;
    private int shakeX, shakeY;
    private String[] lines;
    private int lineIndex;
    private int holdFrames;
    private String[] loopTexts;
    private String loopFinalText;
    private int loopRound, loopBeat;
    private int laughCount;
    private String subtitle;
    private boolean isSubtitleRed;
    private Texture replayBird, replayTube;
    private String scoreText;

    public ScreenEaster(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        random = new Random();

        lairTexture = new Texture("easter/lair_bg.png");
        lairTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        whiteTexture = new Texture("easter/white.png");
        buttonTexture = new Texture("button_bg.png");
        featherTexture = new Texture("particles/feather.png");
        featherTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        birdTextures = new Texture[MyGdxGame.BIRD_FOLDERS.length];
        for (int i = 0; i < birdTextures.length; i++) {
            birdTextures[i] = new Texture(MyGdxGame.BIRD_FOLDERS[i] + "/bird0.png");
        }
        tubeTextures = new Texture[MyGdxGame.MAP_UPPER_TUBES.length];
        for (int i = 0; i < tubeTextures.length; i++) {
            tubeTextures[i] = new Texture(MyGdxGame.MAP_UPPER_TUBES[i]);
        }

        defaultFont = new BitmapFont();
        defaultFont.getData().setScale(5f);
        font = new BitmapFont(Gdx.files.internal("fonts/cyrillic.fnt"));
        bigFont = new BitmapFont(Gdx.files.internal("fonts/cyrillic.fnt"));
        bigFont.getData().setScale(1.5f);

        // места надписей считаем один раз, как в TextButton
        labelX = new int[RESTART_LABELS.length];
        labelY = new int[RESTART_LABELS.length];
        for (int i = 0; i < RESTART_LABELS.length; i++) {
            GlyphLayout gl = new GlyphLayout(getLabelFont(i), RESTART_LABELS[i]);
            labelX[i] = BUTTON_X + (buttonTexture.getWidth() - (int) gl.width) / 2;
            labelY[i] = RESTART_BUTTON_Y + (buttonTexture.getHeight() + (int) gl.height) / 2;
        }
        GlyphLayout menuGl = new GlyphLayout(defaultFont, MENU_LABEL);
        menuLabelX = BUTTON_X + (buttonTexture.getWidth() - (int) menuGl.width) / 2;
        menuLabelY = MENU_BUTTON_Y + (buttonTexture.getHeight() + (int) menuGl.height) / 2;
        GlyphLayout crossGl = new GlyphLayout(defaultFont, CROSS_TEXT);
        crossHalfWidth = (int) crossGl.width / 2;
        crossHalfHeight = (int) crossGl.height / 2;

        portrait = new MadPortrait();
        typewriter = new Typewriter();
        feathers = new Particle[FEATHER_COUNT];
        for (int i = 0; i < FEATHER_COUNT; i++) {
            feathers[i] = new Particle();
        }
        lines = new String[LINE_COUNT];
        loopTexts = new String[LOOP_WORD_COUNT];
    }

    // латинские надписи пишем обычным шрифтом, как настоящую кнопку, а русские - шрифтом с кириллицей
    private BitmapFont getLabelFont(int labelIndex) {
        if (labelIndex < FIRST_RUSSIAN_LABEL) {
            return defaultFont;
        }
        return bigFont;
    }

    // перед сценой: собираем реплики из настоящих чисел игрока и возвращаем всё к началу
    @Override
    public void show() {
        int mapIndex = myGdxGame.screenGame.getMapIndex();
        replayBird = birdTextures[myGdxGame.screenGame.getBirdIndex()];
        replayTube = tubeTextures[mapIndex];
        scoreText = "Score: " + myGdxGame.screenGame.getGamePoints();

        int best = myGdxGame.bestScore;
        String tubeWord;
        lines[0] = "Я тебе когда-нибудь говорил…\nчто такое безумие?";
        lines[1] = "Безумие — это делать одно и то же…\nснова… и снова…";
        lines[2] = "…и ждать, что что-то ИЗМЕНИТСЯ.";
        lines[3] = "Пять смертей. Твой рекорд — " + best + ".\n" + numberToWords(best) + ".";
        if (mapIndex == MyGdxGame.MAP_CAVE) {
            lines[4] = "Здесь, внизу, никто не услышит,\nкак ты разбиваешься.";
            tubeWord = "Камень";
        } else {
            lines[4] = "Этому городу плевать,\nсколько раз ты падаешь.";
            tubeWord = "Труба";
        }
        lines[5] = "Пять раз, амиго. ПЯТЬ.";
        lines[6] = "И знаешь, что самое безумное?…\nТы сейчас снова тапнешь.";
        lines[7] = "…Видишь?";
        lines[8] = "Может, это не безумие.\nМожет, это… упрямство.";
        lines[9] = "Люблю упрямых. Держи мои перья.";

        // в повторе слова добавляются по одному
        loopTexts[0] = "Тап.";
        loopTexts[1] = "Тап. Взмах.";
        loopTexts[2] = "Тап. Взмах. " + tubeWord + ".";
        loopTexts[3] = "Тап. Взмах. " + tubeWord + ". Смерть.";
        loopFinalText = "ТапВзмах" + tubeWord + "Смерть.";

        portrait.reset();
        for (Particle feather : feathers) {
            feather.stop();
        }
        glitchLevel = 0;
        flashFrames = 0;
        shake = 0;
        subtitle = "";
        isSubtitleRed = false;
        step = STEP_GLITCH;
        stepFrame = 0;
    }

    // число словами: 3 - "ТРИ", 25 - "ДВАДЦАТЬ ПЯТЬ"
    private String numberToWords(int number) {
        if (number < NUMBER_WORDS.length) {
            return NUMBER_WORDS[number];
        }
        if (number < TENS_WORDS.length * 10) {
            String words = TENS_WORDS[number / 10];
            if (number % 10 > 0) {
                words += " " + NUMBER_WORDS[number % 10];
            }
            return words;
        }
        return "" + number; // если набрал больше 99, пусть будут цифры
    }

    @Override
    public void render(float delta) {
        stepFrame++;
        if (step == STEP_GLITCH) {
            updateGlitch();
        } else if (step == STEP_DARK) {
            updateDark();
        } else if (step == STEP_ENTER) {
            updateEnter();
        } else if (step == STEP_TALK) {
            updateTalk();
        } else if (step == STEP_LOOP) {
            updateLoop();
        } else if (step == STEP_WAIT_TAP) {
            updateWaitTap();
        } else if (step == STEP_LAUGH) {
            updateLaugh();
        } else {
            updateReward();
        }

        // шаги идут по порядку, поэтому начиная с выезда портрет есть всегда
        if (step >= STEP_ENTER) {
            portrait.move();
        }
        updateShake();
        draw();
    }

    private void updateGlitch() {
        myGdxGame.menuBackground.move();
        for (int i = 0; i < GLITCH_LEVEL_FRAMES.length; i++) {
            if (stepFrame == GLITCH_LEVEL_FRAMES[i]) {
                glitchLevel = i + 1;
                flashFrames = FLASH_FRAMES;
                myGdxGame.sounds.stopMusic(); // на первом сбое музыка обрывается, дальше она уже стоит
                myGdxGame.sounds.playGlitch();
            }
        }
        if (glitchLevel > 0) {
            shake = glitchLevel * GLITCH_SHAKE_STEP; // чем сильнее сломано, тем сильнее трясёт
        }
        if (stepFrame == GLITCH_FRAMES) {
            startDark();
        }
    }

    private void startDark() {
        step = STEP_DARK;
        stepFrame = 0;
        shake = 0;
        myGdxGame.sounds.playHeartbeat();
    }

    private void updateDark() {
        if (stepFrame == SECOND_HEARTBEAT_FRAME) {
            myGdxGame.sounds.playHeartbeat();
        }
        if (stepFrame == DARK_FRAMES) {
            startEnter();
        }
    }

    private void startEnter() {
        step = STEP_ENTER;
        stepFrame = 0;
        myGdxGame.sounds.playEasterMusic();
    }

    private void updateEnter() {
        if (portrait.isInPlace()) {
            startLine(0);
        }
    }

    private void startLine(int index) {
        step = STEP_TALK;
        stepFrame = 0;
        lineIndex = index;
        holdFrames = 0;
        subtitle = ""; // иначе старая реплика на один кадр мелькнёт цветом новой
        typewriter.start(lines[index]);
        portrait.setFace(LINE_FACES[index]);
        isSubtitleRed = LINE_SHOUTS[index];
    }

    private void updateTalk() {
        if (!typewriter.isFinished()) {
            if (typewriter.update()) {
                portrait.talk();
                myGdxGame.sounds.playBlip(BLIP_PITCH_MIN + random.nextFloat() * BLIP_PITCH_RANGE);
            }
            subtitle = typewriter.getShownText();
        } else if (lineIndex == LINE_BEFORE_TAP) {
            startWaitTap(); // здесь не ждём: тап игрока должен засчитаться сразу
        } else {
            holdFrames++;
            if (holdFrames == HOLD_FRAMES) {
                nextAfterLine();
            }
        }
        if (LINE_SHOUTS[lineIndex]) {
            shake = SHOUT_SHAKE; // кричит - дрожит
        }
    }

    private void nextAfterLine() {
        if (lineIndex == LINE_BEFORE_LOOP) {
            startLoop();
        } else if (lineIndex == LINE_BEFORE_LAUGH) {
            startLaugh();
        } else if (lineIndex == LINE_COUNT - 1) {
            startReward();
        } else {
            startLine(lineIndex + 1);
        }
    }

    private void startLoop() {
        step = STEP_LOOP;
        stepFrame = 0;
        loopRound = 0;
        loopBeat = 0;
        isSubtitleRed = false;
        startBeat();
    }

    // одно слово повтора: птица игрока делает следующий шаг к своей смерти
    private void startBeat() {
        subtitle = loopTexts[loopBeat];
        portrait.talk();
        if (loopBeat == LOOP_WORD_COUNT - 1) {
            myGdxGame.sounds.playHit(); // "Смерть."
            shake = DEATH_SHAKE;
            flashFrames = FLASH_FRAMES;
        } else {
            myGdxGame.sounds.playFlap();
        }
    }

    private void updateLoop() {
        if (loopRound == BEAT_FRAMES.length) {
            // финал: все слова слились в одно
            shake = FINAL_SHAKE;
            if (stepFrame == LOOP_FINAL_FRAMES) {
                startLine(LINE_BEFORE_LOOP + 1);
            }
        } else if (stepFrame == BEAT_FRAMES[loopRound]) {
            stepFrame = 0;
            loopBeat++;
            if (loopBeat == LOOP_WORD_COUNT) {
                loopBeat = 0;
                loopRound++; // следующий круг, он быстрее
            }
            if (loopRound == BEAT_FRAMES.length) {
                startLoopFinal();
            } else {
                startBeat();
            }
        }
    }

    private void startLoopFinal() {
        subtitle = loopFinalText;
        isSubtitleRed = true;
        loopBeat = LOOP_WORD_COUNT - 1; // на картинке остаётся смерть
        portrait.setFace(MadPortrait.FACE_SHOUT);
        myGdxGame.sounds.playHit();
        flashFrames = FLASH_FRAMES;
    }

    private void startWaitTap() {
        step = STEP_WAIT_TAP;
        stepFrame = 0;
    }

    private void updateWaitTap() {
        if (Gdx.input.justTouched()) {
            startLine(LINE_BEFORE_TAP + 1); // "…Видишь?"
        }
    }

    private void startLaugh() {
        step = STEP_LAUGH;
        stepFrame = 0;
        laughCount = 0;
        portrait.setFace(MadPortrait.FACE_LAUGH);
    }

    private void updateLaugh() {
        if (stepFrame % LAUGH_PERIOD == 0) {
            myGdxGame.sounds.playBlip(LAUGH_START_PITCH - laughCount * LAUGH_PITCH_STEP);
            laughCount++;
        }
        shake = LAUGH_SHAKE;
        if (stepFrame == LAUGH_FRAMES) {
            startLine(LINE_BEFORE_LAUGH + 1);
        }
    }

    private void startReward() {
        step = STEP_REWARD;
        stepFrame = 0;
        myGdxGame.isMadBirdUnlocked = true;
        myGdxGame.achievementTracker.onEasterEgg();
        myGdxGame.sounds.playAchievement();

        // взрыв красных перьев: каждое перо летит в свою случайную сторону
        for (Particle feather : feathers) {
            float speedX = random.nextFloat() * 2 * FEATHER_SPEED_X - FEATHER_SPEED_X;
            float speedY = FEATHER_SPEED_Y_MIN + random.nextFloat() * FEATHER_SPEED_Y_RANGE;
            float size = FEATHER_SIZE + random.nextInt(FEATHER_SIZE / 2);
            int life = FEATHER_LIFE + random.nextInt(FEATHER_LIFE / 2);
            Color color = FEATHER_COLORS[random.nextInt(FEATHER_COLORS.length)];
            feather.start(FEATHER_X, FEATHER_Y, speedX, speedY, size, life, color);
        }
    }

    private void updateReward() {
        for (Particle feather : feathers) {
            if (feather.isAlive()) {
                feather.move(0, 0f); // мир стоит, поэтому 0: перья просто разлетаются
            }
        }
        if (stepFrame == HINT_FRAME) {
            subtitle = HINT_TEXT;
            isSubtitleRed = false;
        }
        boolean isTapped = stepFrame > HINT_FRAME && Gdx.input.justTouched();
        if (isTapped || stepFrame == REWARD_FRAMES) {
            myGdxGame.setScreen(myGdxGame.screenRestart);
        }
    }

    // тряска: каждый кадр случайный сдвиг, и она понемногу затихает. Заодно гаснет красная вспышка
    private void updateShake() {
        if (shake > 0) {
            shakeX = random.nextInt(2 * shake + 1) - shake;
            shakeY = random.nextInt(2 * shake + 1) - shake;
            shake--;
        } else {
            shakeX = 0;
            shakeY = 0;
        }
        if (flashFrames > 0) {
            flashFrames--;
        }
    }

    private void draw() {
        myGdxGame.beginDraw();
        if (step == STEP_GLITCH) {
            drawFakeRestart();
        } else {
            drawLair();
            if (step == STEP_LOOP) {
                drawReplay();
            }
            if (step >= STEP_ENTER) {
                portrait.draw(myGdxGame.batch, shakeX, shakeY);
            }
            if (step >= STEP_TALK) {
                drawSubtitle();
            }
            if (step == STEP_REWARD) {
                drawReward();
            }
        }
        drawFlash();
        myGdxGame.batch.end();
    }

    // выглядит как настоящий рестарт, только всё рисуем сами, чтобы оно могло трястись
    private void drawFakeRestart() {
        myGdxGame.menuBackground.draw(myGdxGame.batch);
        myGdxGame.batch.draw(buttonTexture, BUTTON_X + shakeX, RESTART_BUTTON_Y + shakeY);
        myGdxGame.batch.draw(buttonTexture, BUTTON_X - shakeX, MENU_BUTTON_Y + shakeY);

        BitmapFont labelFont = getLabelFont(glitchLevel);
        if (glitchLevel == RESTART_LABELS.length - 1) {
            labelFont.setColor(SHOUT_COLOR);
        } else {
            labelFont.setColor(Color.WHITE);
        }
        labelFont.draw(myGdxGame.batch, RESTART_LABELS[glitchLevel], labelX[glitchLevel] + shakeX, labelY[glitchLevel] + shakeY);

        defaultFont.setColor(Color.WHITE);
        defaultFont.draw(myGdxGame.batch, MENU_LABEL, menuLabelX - shakeX, menuLabelY + shakeY);
        defaultFont.draw(myGdxGame.batch, scoreText, SCORE_X + shakeX, SCORE_Y - shakeY);
        myGdxGame.achievementTracker.drawList(myGdxGame.batch);
    }

    private void drawLair() {
        float alpha = 1f;
        if (step == STEP_DARK) {
            // логово проступает из темноты: прозрачность растёт от 0 до 1
            alpha = (float) (stepFrame - LAIR_FADE_START) / (DARK_FRAMES - LAIR_FADE_START);
            if (alpha < 0) {
                alpha = 0;
            }
        }
        myGdxGame.batch.setColor(1f, 1f, 1f, alpha);
        myGdxGame.batch.draw(lairTexture, -MARGIN + shakeX, -MARGIN + shakeY,
            MyGdxGame.SCR_WIDTH + 2 * MARGIN, MyGdxGame.SCR_HEIGHT + 2 * MARGIN);
        myGdxGame.batch.setColor(Color.WHITE);
    }

    // повтор смерти: птица игрока, колонна его карты и красный крест
    private void drawReplay() {
        if (loopBeat >= TUBE_WORD) {
            myGdxGame.batch.draw(replayTube, REPLAY_TUBE_X + shakeX, REPLAY_TUBE_Y + shakeY, REPLAY_TUBE_WIDTH, REPLAY_TUBE_HEIGHT);
        }
        int birdHeight = replayBird.getHeight() * REPLAY_BIRD_WIDTH / replayBird.getWidth();
        int birdX = REPLAY_BIRD_X[loopBeat] + shakeX;
        int birdY = REPLAY_BIRD_Y[loopBeat] + shakeY;
        myGdxGame.batch.draw(replayBird, birdX, birdY, REPLAY_BIRD_WIDTH, birdHeight);

        if (loopBeat == LOOP_WORD_COUNT - 1) {
            defaultFont.setColor(Color.RED);
            defaultFont.draw(myGdxGame.batch, CROSS_TEXT, birdX + REPLAY_BIRD_WIDTH / 2 - crossHalfWidth,
                birdY + birdHeight / 2 + crossHalfHeight);
            defaultFont.setColor(Color.WHITE);
        }
    }

    private void drawSubtitle() {
        myGdxGame.batch.setColor(0f, 0f, 0f, SUBTITLE_BOX_ALPHA); // белый квадрат стал полупрозрачным чёрным
        myGdxGame.batch.draw(whiteTexture, 0, 0, MyGdxGame.SCR_WIDTH, SUBTITLE_BOX_HEIGHT);
        myGdxGame.batch.setColor(Color.WHITE);

        if (isSubtitleRed) {
            font.setColor(SHOUT_COLOR);
        } else {
            font.setColor(Color.WHITE);
        }
        font.draw(myGdxGame.batch, subtitle, SUBTITLE_X + shakeX, SUBTITLE_Y + shakeY);
    }

    private void drawReward() {
        for (Particle feather : feathers) {
            if (feather.isAlive()) {
                feather.draw(myGdxGame.batch, featherTexture);
            }
        }
        myGdxGame.batch.setColor(Color.WHITE); // частица красит batch, возвращаем белый

        bigFont.setColor(Color.GOLD);
        bigFont.draw(myGdxGame.batch, REWARD_TEXT, REWARD_X, REWARD_Y);
        Texture madBird = birdTextures[MyGdxGame.BIRD_MAD];
        int madBirdHeight = madBird.getHeight() * REWARD_BIRD_WIDTH / madBird.getWidth();
        myGdxGame.batch.draw(madBird, REWARD_BIRD_X, REWARD_BIRD_Y, REWARD_BIRD_WIDTH, madBirdHeight);
    }

    private void drawFlash() {
        if (flashFrames > 0) {
            myGdxGame.batch.setColor(1f, 0f, 0f, FLASH_ALPHA * flashFrames / FLASH_FRAMES);
            myGdxGame.batch.draw(whiteTexture, 0, 0, MyGdxGame.SCR_WIDTH, MyGdxGame.SCR_HEIGHT);
            myGdxGame.batch.setColor(Color.WHITE);
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        lairTexture.dispose();
        whiteTexture.dispose();
        buttonTexture.dispose();
        featherTexture.dispose();
        for (Texture texture : birdTextures) {
            texture.dispose();
        }
        for (Texture texture : tubeTextures) {
            texture.dispose();
        }
        defaultFont.dispose();
        font.dispose();
        bigFont.dispose();
        portrait.dispose();
    }
}
