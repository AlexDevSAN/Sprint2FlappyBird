package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

// одна частица: клубок дыма, перо или искра
public class Particle {
    float x, y; // центр частицы
    float speedX, speedY; // сдвиг за кадр в пикселях
    float size; // ширина и высота картинки
    int life; // сколько кадров ещё живёт, 0 - частица свободна
    int maxLife; // сколько кадров живёт с самого начала
    Color color;

    public Particle() {
        color = new Color(); // создаём один раз, дальше только меняем
    }

    // запускаем частицу заново, новый объект не нужен
    public void start(float x, float y, float speedX, float speedY, float size, int life, Color color) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.size = size;
        this.life = life;
        this.maxLife = life;
        this.color.set(color);
    }

    public boolean isAlive() {
        return life > 0;
    }

    // worldSpeed уносит частицу влево вместе с колоннами, growSpeed меняет размер
    public void move(int worldSpeed, float growSpeed) {
        x += speedX - worldSpeed;
        y += speedY;
        size += growSpeed;
        life--;
    }

    public void draw(Batch batch, Texture texture) {
        float fade = (float) life / maxLife; // 1 в начале жизни, 0 в конце
        batch.setColor(color.r, color.g, color.b, color.a * fade);
        batch.draw(texture, x - size / 2, y - size / 2, size, size);
    }
}
