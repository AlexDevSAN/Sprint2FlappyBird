package ru.samsung.gamestudio.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import ru.samsung.gamestudio.MyGdxGame;

// все звуки и музыка игры в одном месте, экраны только вызывают методы
public class GameSounds {
    private static final float MUSIC_VOLUME = 0.4f;
    private static final float FLAP_VOLUME = 0.5f; // взмахов очень много, поэтому потише
    private static final float BLIP_VOLUME = 0.6f; // писк букв в пасхалке

    // короткие звуки (Sound) целиком лежат в памяти и могут звучать сразу
    private Sound flapSound;
    private Sound pointSound;
    private Sound hitSound;
    private Sound clickSound;
    private Sound gameOverSound;
    private Sound achievementSound;
    // звуки пасхалки
    private Sound blipSound;
    private Sound heartbeatSound;
    private Sound glitchSound;

    // музыка (Music) длинная, она читается из файла по кусочку
    private Music menuMusic;
    private Music[] mapMusic; // номер трека = номер карты
    private Music easterMusic; // гул в логове безумной птицы
    private Music currentMusic; // трек, который играет сейчас

    public GameSounds() {
        flapSound = Gdx.audio.newSound(Gdx.files.internal("sounds/flap.wav"));
        pointSound = Gdx.audio.newSound(Gdx.files.internal("sounds/point.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.ogg"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/game_over.ogg"));
        achievementSound = Gdx.audio.newSound(Gdx.files.internal("sounds/achievement.ogg"));
        blipSound = Gdx.audio.newSound(Gdx.files.internal("easter/blip.wav"));
        heartbeatSound = Gdx.audio.newSound(Gdx.files.internal("easter/heartbeat.wav"));
        glitchSound = Gdx.audio.newSound(Gdx.files.internal("easter/glitch.wav"));

        menuMusic = loadMusic("music/menu.ogg");
        mapMusic = new Music[MyGdxGame.MAP_MUSIC.length];
        for (int i = 0; i < mapMusic.length; i++) {
            mapMusic[i] = loadMusic(MyGdxGame.MAP_MUSIC[i]);
        }
        easterMusic = loadMusic("easter/drone.ogg");
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

    // pitch - высота звука: 1 - как записан, больше 1 - выше, меньше 1 - ниже
    public void playBlip(float pitch) {
        blipSound.play(BLIP_VOLUME, pitch, 0); // 0 - звук посередине, не левее и не правее
    }

    public void playHeartbeat() {
        heartbeatSound.play();
    }

    public void playGlitch() {
        glitchSound.play();
    }

    public void playMenuMusic() {
        playMusic(menuMusic);
    }

    public void playMapMusic(int mapIndex) {
        playMusic(mapMusic[mapIndex]);
    }

    public void playEasterMusic() {
        playMusic(easterMusic);
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
        blipSound.dispose();
        heartbeatSound.dispose();
        glitchSound.dispose();
        menuMusic.dispose();
        for (Music music : mapMusic) {
            music.dispose();
        }
        easterMusic.dispose();
    }
}
