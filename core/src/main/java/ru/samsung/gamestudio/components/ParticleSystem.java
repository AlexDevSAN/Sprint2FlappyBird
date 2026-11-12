package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import ru.samsung.gamestudio.character.Bird;

// шлейф за птицей и всплеск при взмахе
public class ParticleSystem {
    private static final int MAX_PARTICLES = 100;
    private static final int TRAIL_PERIOD = 3; // одна частица шлейфа раз в 3 кадра
    private static final int BURST_COUNT = 10; // сколько частиц в одном всплеске

    // всё ниже по номеру птицы: 0 - синяя (дым), 1 - классика (перья), 2 - герой (искры),
    // 3 - секретная Mad (красно-чёрные искры)
    private static final String[] TEXTURE_PATHS = {"particles/smoke.png", "particles/feather.png", "particles/spark.png",
        "particles/spark.png"};
    // у каждой птицы два цвета, частица берёт один из них случайно
    private static final Color[][] COLORS = {
        {new Color(0.95f, 0.97f, 1f, 0.85f), new Color(0.75f, 0.8f, 0.9f, 0.8f)},
        {new Color(1f, 0.85f, 0.2f, 1f), new Color(1f, 0.6f, 0.15f, 1f)},
        {new Color(1f, 0.25f, 0.1f, 1f), new Color(1f, 0.9f, 0.2f, 1f)},
        {new Color(0.9f, 0.05f, 0.05f, 1f), new Color(0.15f, 0f, 0f, 1f)}
    };
    private static final int[] SIZES = {40, 34, 40, 38}; // размер новой частицы
    private static final float[] GROW_SPEEDS = {0.6f, 0f, -0.6f, -0.4f}; // дым растёт, перья нет, искры тают
    private static final float[] RISE_SPEEDS = {0.4f, -0.8f, 0f, 0.3f}; // дым поднимается, перья падают
    private static final int[] LIFE_TIMES = {40, 50, 25, 30}; // сколько кадров живёт частица

    private Particle[] particles; // создаются один раз, потом только переиспользуются
    private Texture[] textures;
    private int skinIndex;
    private int trailCounter;
    private int worldSpeed = 3; // на столько пикселей за кадр частицы уносит влево, как колонны

    public ParticleSystem() {
        particles = new Particle[MAX_PARTICLES];
        for (int i = 0; i < MAX_PARTICLES; i++) {
            particles[i] = new Particle();
        }

        textures = new Texture[TEXTURE_PATHS.length];
        for (int i = 0; i < TEXTURE_PATHS.length; i++) {
            textures[i] = new Texture(TEXTURE_PATHS[i]);
            textures[i].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // сглаживание при масштабе
        }
    }

    public void setSkin(int birdIndex) {
        skinIndex = birdIndex;
    }

    public void setWorldSpeed(int worldSpeed) {
        this.worldSpeed = worldSpeed;
    }

    // гасим все частицы, чтобы новая игра начиналась чистой
    public void clear() {
        for (Particle particle : particles) {
            particle.stop();
        }
    }

    public void addTrail(Bird bird) {
        trailCounter++;
        if (trailCounter == TRAIL_PERIOD) {
            trailCounter = 0;
            float x = bird.getX() + bird.getWidth() * 0.2f; // у хвоста
            float y = bird.getY() + bird.getHeight() * 0.5f;
            add(x, y, MathUtils.random(-1f, 0f), RISE_SPEEDS[skinIndex] + MathUtils.random(-0.3f, 0.3f));
        }
    }

    public void addBurst(Bird bird) {
        float x = bird.getX() + bird.getWidth() * 0.4f; // под крылом
        float y = bird.getY() + bird.getHeight() * 0.4f;
        for (int i = 0; i < BURST_COUNT; i++) {
            add(x, y, MathUtils.random(-3f, 1f), MathUtils.random(-3f, 1f)); // в основном назад и вниз
        }
    }

    // берём первую свободную частицу, если все заняты - новой не будет
    private void add(float x, float y, float speedX, float speedY) {
        for (Particle particle : particles) {
            if (!particle.isAlive()) {
                Color color = COLORS[skinIndex][MathUtils.random(1)];
                float size = SIZES[skinIndex] + MathUtils.random(-4, 4);
                particle.start(x, y, speedX, speedY, size, LIFE_TIMES[skinIndex], color);
                return;
            }
        }
    }

    public void move() {
        for (Particle particle : particles) {
            if (particle.isAlive()) {
                particle.move(worldSpeed, GROW_SPEEDS[skinIndex]);
            }
        }
    }

    public void draw(Batch batch) {
        for (Particle particle : particles) {
            if (particle.isAlive()) {
                particle.draw(batch, textures[skinIndex]);
            }
        }
        batch.setColor(Color.WHITE); // вернуть белый, иначе птица и колонны тоже покрасятся
    }

    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
}
