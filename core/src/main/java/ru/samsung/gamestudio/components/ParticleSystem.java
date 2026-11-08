package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import ru.samsung.gamestudio.character.Bird;

// шлейф за птицей и всплеск при взмахе
public class ParticleSystem {
    final int maxParticles = 100;
    Particle[] particles; // создаются один раз, потом только переиспользуются

    public int worldSpeed = 3; // как speed у Tube: на столько пикселей частицы уносит влево за кадр

    final int trailPeriod = 3; // одна частица шлейфа раз в 3 кадра
    int trailCounter;
    final int burstCount = 10; // сколько частиц в одном всплеске

    // всё ниже по номеру птицы: 0 - синяя (дым), 1 - классика (перья), 2 - герой (искры)
    int skinIndex;
    final String[] texturePaths = {"particles/smoke.png", "particles/feather.png", "particles/spark.png"};
    Texture[] textures;
    // у каждой птицы два цвета, частица берёт один из них случайно
    final Color[][] colors = {
        {new Color(0.95f, 0.97f, 1f, 0.85f), new Color(0.75f, 0.8f, 0.9f, 0.8f)},
        {new Color(1f, 0.85f, 0.2f, 1f), new Color(1f, 0.6f, 0.15f, 1f)},
        {new Color(1f, 0.25f, 0.1f, 1f), new Color(1f, 0.9f, 0.2f, 1f)}
    };
    final int[] sizes = {40, 34, 40}; // размер новой частицы
    final float[] growSpeeds = {0.6f, 0f, -0.6f}; // дым растёт, перья нет, искры тают
    final float[] riseSpeeds = {0.4f, -0.8f, 0f}; // дым поднимается, перья падают
    final int[] lifeTimes = {40, 50, 25}; // сколько кадров живёт частица

    public ParticleSystem() {
        particles = new Particle[maxParticles];
        for (int i = 0; i < maxParticles; i++) {
            particles[i] = new Particle();
        }

        textures = new Texture[texturePaths.length];
        for (int i = 0; i < texturePaths.length; i++) {
            textures[i] = new Texture(texturePaths[i]);
            textures[i].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // сглаживание при масштабе
        }
    }

    public void setSkin(int birdIndex) {
        skinIndex = birdIndex;
    }

    // гасим все частицы, чтобы новая игра начиналась чистой
    public void clear() {
        for (Particle particle : particles) {
            particle.life = 0;
        }
    }

    public void addTrail(Bird bird) {
        trailCounter++;
        if (trailCounter == trailPeriod) {
            trailCounter = 0;
            float x = bird.x + bird.width * 0.2f; // у хвоста
            float y = bird.y + bird.height * 0.5f;
            add(x, y, MathUtils.random(-1f, 0f), riseSpeeds[skinIndex] + MathUtils.random(-0.3f, 0.3f));
        }
    }

    public void addBurst(Bird bird) {
        float x = bird.x + bird.width * 0.4f; // под крылом
        float y = bird.y + bird.height * 0.4f;
        for (int i = 0; i < burstCount; i++) {
            add(x, y, MathUtils.random(-3f, 1f), MathUtils.random(-3f, 1f)); // в основном назад и вниз
        }
    }

    // берём первую свободную частицу, если все заняты - новой не будет
    void add(float x, float y, float speedX, float speedY) {
        for (Particle particle : particles) {
            if (!particle.isAlive()) {
                Color color = colors[skinIndex][MathUtils.random(1)];
                float size = sizes[skinIndex] + MathUtils.random(-4, 4);
                particle.start(x, y, speedX, speedY, size, lifeTimes[skinIndex], color);
                return;
            }
        }
    }

    public void move() {
        for (Particle particle : particles) {
            if (particle.isAlive()) {
                particle.move(worldSpeed, growSpeeds[skinIndex]);
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
