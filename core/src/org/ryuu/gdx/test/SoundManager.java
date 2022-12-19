package org.ryuu.gdx.test;

import com.badlogic.gdx.audio.Sound;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ryuu.functional.IAction;
import org.ryuu.functional.IFunc1Arg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class SoundManager {
    private static final List<IAction> playSoundList = new ArrayList<>();
    private static final List<IAction> executePlaySoundList = new ArrayList<>();

    @Getter
    @Setter
    private static IFunc1Arg<String, Sound> getSoundByPath;
    private static long interval = 0;

    static {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            synchronized (playSoundList) {
                if (!playSoundList.isEmpty()) {
                    executePlaySoundList.addAll(playSoundList);
                    playSoundList.clear();
                }
                while (!executePlaySoundList.isEmpty()) {
                    executePlaySoundList.remove(0).invoke();
                }
            }
        }, 0, interval, MILLISECONDS);
    }

    public static void init(long interval, IFunc1Arg<String, Sound> getSoundByPath) {
        SoundManager.interval = interval;
        SoundManager.getSoundByPath = getSoundByPath;
    }

    public static SoundWrapper play(String path) {
        synchronized (playSoundList) {
            SoundWrapper soundWrapper = new SoundWrapper();
            playSoundList.add(() -> {
                Sound sound = getSoundByPath.invoke(path);
                soundWrapper.sound = sound;
                soundWrapper.soundId = sound.play();
            });
            return soundWrapper;
        }
    }

    public static SoundWrapper play(String path, float volume) {
        synchronized (playSoundList) {
            SoundWrapper soundWrapper = new SoundWrapper();
            playSoundList.add(() -> {
                Sound sound = getSoundByPath.invoke(path);
                soundWrapper.sound = sound;
                soundWrapper.soundId = sound.play(volume);
            });
            return soundWrapper;
        }
    }

    public static SoundWrapper play(String path, float volume, float pitch, float pan) {
        synchronized (playSoundList) {
            SoundWrapper soundWrapper = new SoundWrapper();
            playSoundList.add(() -> {
                Sound sound = getSoundByPath.invoke(path);
                soundWrapper.sound = sound;
                soundWrapper.soundId = sound.play(volume, pitch, pan);
            });
            return soundWrapper;
        }
    }

    public static SoundWrapper loop(String path) {
        synchronized (playSoundList) {
            SoundWrapper soundWrapper = new SoundWrapper();
            playSoundList.add(() -> {
                Sound sound = getSoundByPath.invoke(path);
                soundWrapper.sound = sound;
                soundWrapper.soundId = sound.loop();
            });
            return soundWrapper;
        }
    }

    public static SoundWrapper loop(String path, float volume) {
        synchronized (playSoundList) {
            SoundWrapper soundWrapper = new SoundWrapper();
            playSoundList.add(() -> {
                Sound sound = getSoundByPath.invoke(path);
                soundWrapper.sound = sound;
                soundWrapper.soundId = sound.loop(volume);
            });
            return soundWrapper;
        }
    }

    public static SoundWrapper loop(String path, float volume, float pitch, float pan) {
        synchronized (playSoundList) {
            SoundWrapper soundWrapper = new SoundWrapper();
            playSoundList.add(() -> {
                Sound sound = getSoundByPath.invoke(path);
                soundWrapper.sound = sound;
                soundWrapper.soundId = sound.loop(volume, pitch, pan);
            });
            return soundWrapper;
        }
    }

    @ToString
    public static class SoundWrapper {
        @Getter
        private long soundId;
        @Getter
        private Sound sound;
    }
}