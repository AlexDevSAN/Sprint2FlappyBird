package ru.samsung.gamestudio.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.samsung.gamestudio.MyGdxGame;

// безумная птица из пасхалки: портрет внизу справа, говорит, моргает и дышит
public class MadPortrait {
    // выражения лица
    public static final int FACE_SMIRK = 0; // ухмылка
    public static final int FACE_SHOUT = 1; // крик
    public static final int FACE_LAUGH = 2; // хохот
    public static final int FACE_POINT = 3; // показывает крылом на игрока
    public static final int FACE_CALM = 4; // спокойный, палец вверх

    // номер картинки easter/madN.png для каждого лица: с закрытым клювом и с открытым.
    // у крика, хохота и жеста клюв открыт всегда, поэтому картинка одна и та же
    private static final int[] CLOSED_IMAGES = {0, 3, 4, 5, 6};
    private static final int[] OPEN_IMAGES = {1, 3, 4, 5, 7};
    private static final int BLINK_IMAGE = 2; // моргает только ухмылка
    private static final int IMAGE_COUNT = 8;

    private static final int SIZE = 443; // картинки квадратные
    private static final int STOP_X = 790; // здесь портрет останавливается
    private static final int Y = -20; // чуть ниже края экрана, чтобы при тряске снизу не было щели
    private static final int ENTER_SPEED = 14; // выезжает справа на 14 пикселей за кадр
    private static final float ZOOM_SPEED = 0.00012f; // медленный наезд: портрет понемногу растёт
    private static final float MAX_ZOOM = 1.18f;

    private static final int BEAK_FRAMES = 4; // пока говорит, клюв 4 кадра открыт и 4 закрыт
    private static final int SILENCE_FRAMES = 8; // столько кадров без новых букв, и клюв закрывается
    private static final int BLINK_PERIOD = 110; // моргает раз в 110 кадров
    private static final int BLINK_FRAMES = 7; // и 7 кадров держит глаза закрытыми
    private static final int BREATH_PERIOD = 30; // дышит: каждые 30 кадров чуть вверх или вниз
    private static final int BREATH_HEIGHT = 3;

    private Texture[] images;
    private int face;
    private int x;
    private float zoom;
    private int frameCounter;
    private int silentFrames; // сколько кадров не было новых букв

    public MadPortrait() {
        images = new Texture[IMAGE_COUNT];
        for (int i = 0; i < IMAGE_COUNT; i++) {
            images[i] = new Texture("easter/mad" + i + ".png");
            images[i].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // при увеличении без лесенки
        }
        reset();
    }

    // перед каждой сценой: портрет снова за правым краем экрана
    public void reset() {
        face = FACE_SMIRK;
        x = MyGdxGame.SCR_WIDTH;
        zoom = 1f;
        frameCounter = 0;
        silentFrames = SILENCE_FRAMES;
    }

    public void setFace(int face) {
        this.face = face;
    }

    // напечаталась буква: клюв двигается ещё немного
    public void talk() {
        silentFrames = 0;
    }

    public boolean isInPlace() {
        return x == STOP_X;
    }

    public void move() {
        if (x > STOP_X) {
            x -= ENTER_SPEED;
            if (x < STOP_X) {
                x = STOP_X; // дальше места не проезжаем
            }
        } else if (zoom < MAX_ZOOM) {
            zoom += ZOOM_SPEED; // наезд начинается, когда портрет на месте
        }
        if (silentFrames < SILENCE_FRAMES) {
            silentFrames++;
        }
        frameCounter++;
    }

    // shakeX и shakeY - сдвиг для тряски, его считает экран
    public void draw(Batch batch, int shakeX, int shakeY) {
        int image = CLOSED_IMAGES[face];
        boolean isTalking = silentFrames < SILENCE_FRAMES;
        if (isTalking && frameCounter / BEAK_FRAMES % 2 == 0) {
            image = OPEN_IMAGES[face];
        } else if (!isTalking && face == FACE_SMIRK && frameCounter % BLINK_PERIOD < BLINK_FRAMES) {
            image = BLINK_IMAGE;
        }

        int breath = 0;
        if (frameCounter / BREATH_PERIOD % 2 == 1) {
            breath = BREATH_HEIGHT;
        }

        // портрет растёт от середины: середина стоит на месте, низ остаётся у края экрана
        int size = (int) (SIZE * zoom);
        int drawX = x - (size - SIZE) / 2 + shakeX;
        int drawY = Y + breath + shakeY;
        batch.draw(images[image], drawX, drawY, size, size);
    }

    public void dispose() {
        for (Texture image : images) {
            image.dispose();
        }
    }
}
