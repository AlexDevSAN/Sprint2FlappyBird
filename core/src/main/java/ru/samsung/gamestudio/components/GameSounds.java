package ru.samsung.gamestudio.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

// все звуки и музыка игры в одном месте, экраны только вызывают методы
public class GameSounds {
    // короткие звуки (Sound) целиком лежат в памяти и могут звучать сразу
    Sound flapSound;
    Sound pointSound;
    Sound hitSound;
    Sound clickSound;
    Sound gameOverSound;
    Sound achievementSound;

    // музыка (Music) длинная, она читается из файла по кусочку
    Music menuMusic;
    Music[] mapMusic; // музыка карт: 0 - город, 1 - пещера
    Music currentMusic; // трек, который играет сейчас

    final float musicVolume = 0.4f;
    final float flapVolume = 0.5f; // взмахов очень много, поэтому потише

    public GameSounds() {
        flapSound = Gdx.audio.newSound(Gdx.files.internal("sounds/flap.wav"));
        pointSound = Gdx.audio.newSound(Gdx.files.internal("sounds/point.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.ogg"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/game_over.ogg"));
        achievementSound = Gdx.audio.newSound(Gdx.files.internal("sounds/achievement.ogg"));

        menuMusic = loadMusic("music/menu.ogg");
        // номер трека = номер карты в ScreenGame
        mapMusic = new Music[]{
            loadMusic("music/city.ogg"),
            loadMusic("music/cave.ogg")
        };
    }

    private Music loadMusic(String path) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.setLooping(true); // играет по кругу
        music.setVolume(musicVolume);
        return music;
    }

    public void playFlap() {
        flapSound.play(flapVolume);
    }

    public void playPoint() {
        pointSound.play();
    }

    public void playHit() {
        hitSound.play();
    }

    public void playClick() {
        clickSound.play();
    }

    public void playGameOver() {
        gameOverSound.play();
    }

    // звук для достижений
    public void playAchievement() {
        achievementSound.play();
    }

    public void playMenuMusic() {
        playMusic(menuMusic);
    }

    public void playMapMusic(int mapIndex) {
        playMusic(mapMusic[mapIndex]);
    }

    // старый трек выключаем, новый включаем с начала
    private void playMusic(Music music) {
        stopMusic();
        currentMusic = music;
        currentMusic.play();
    }

    public void stopMusic() {
        if (currentMusic != null) { // в самом начале игры трека ещё нет
            currentMusic.stop();
        }
    }

    public void dispose() {
        flapSound.dispose();
        pointSound.dispose();
        hitSound.dispose();
        clickSound.dispose();
        gameOverSound.dispose();
        achievementSound.dispose();
        menuMusic.dispose();
        for (Music music : mapMusic) {
            music.dispose();
        }
    }
}
