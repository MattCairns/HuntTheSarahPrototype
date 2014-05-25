package com.matthewcairns.flameblade.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matthew Cairns on 25/05/2014.
 * All rights reserved.
 */
public class AudioController {
    Map<String, Music> musicMap;
    Map<String, Sound> soundMap;


    public AudioController() {
        Music mainGameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/swashingthebuck.ogg"));
        Music mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/thesagabegins.ogg"));

        musicMap = new HashMap<String, Music>();
        musicMap.put("Swashing the Buck", mainGameMusic);
        musicMap.put("The Saga Begins", mainMenuMusic);


        Sound playerWalk = Gdx.audio.newSound(Gdx.files.internal("sounds/footstep06.ogg"));
        Sound playerHurt = Gdx.audio.newSound(Gdx.files.internal("sounds/player_hurt.mp3"));
        Sound playerShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/arrow_shoot.mp3"));

        soundMap = new HashMap<String, Sound>();
        soundMap.put("Footsteps", playerWalk);
        soundMap.put("Player Hurt", playerHurt);
        soundMap.put("Player Shoot", playerShoot);

    }

    public Sound getSound(String name) {
        return soundMap.get(name);
    }

    public Music getMusic(String name) {
        return musicMap.get(name);
    }

}
