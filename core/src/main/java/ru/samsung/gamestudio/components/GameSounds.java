package ru.samsung.gamestudio.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import ru.samsung.gamestudio.MyGdxGame;

// все звуки и музыка игры в одном месте, экраны только вызывают методы
public class GameSounds {
    private static final float MUSIC_VOLUME = 0.4f;
    private static final float FLAP_VOLUME = 0.5f; // взмахов очень много, поэтому потише

    // короткие звуки (Sound) целиком лежат в памяти и могут звучать сразу
    private Sound flapSound;
    private Sound pointSound;
    private Sound hitSound;
    private Sound clickSound;
    private Sound gameOverSound;
    private Sound achievementSound;

    // музыка (Music) длинная, она читается из файла по кусочку
    private Music menuMusic;
    private Music[] mapMusic; // номер трека = номер карты
    private Music currentMusic; // трек, который играет сейчас

    public GameSounds() {
        flapSound = Gdx.audio.newSound(Gdx.files.internal("sounds/flap.wav"));
        pointSound = Gdx.audio.newSound(Gdx.files.internal("sounds/point.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.ogg"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/game_over.ogg"));
        achievementSound = Gdx.audio.newSound(Gdx.files.internal("sounds/achievement.ogg"));

        menuMusic = loadMusic("music/menu.ogg");
        mapMusic = new Music[MyGdxGame.MAP_MUSIC.length];
        for (int i = 0; i < mapMusic.length; i++) {
            mapMusic[i] = loadMusic(MyGdxGame.MAP_MUSIC[i]);
        }
        currentMusic = menuMusic; // пока ничего не играет, но выключать всегда есть что
    }

    private Music loadMusic(String path) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.setLooping(true); // играет по кругу
        music.setVolume(MUSIC_VOLUME);
        return music;
    }

    public void playFlap() {
        flapSound.play(FLAP_VOLUME);
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
        currentMusic.stop();
        currentMusic = music;
        currentMusic.play();
    }

    public void stopMusic() {
        currentMusic.stop();
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
