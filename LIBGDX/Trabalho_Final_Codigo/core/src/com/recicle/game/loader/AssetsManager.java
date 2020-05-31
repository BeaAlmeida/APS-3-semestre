package com.recicle.game.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetsManager {
//load and unload resources for your project.

    public final AssetManager manager = new AssetManager();

    //----------Textures----------


    public final String gameImages = "images/sprites.atlas";
    public final String loadingImages = "images/loading.atlas";

    public void queueAddImages(){
        manager.load(gameImages, TextureAtlas.class);
    }

    public void queueAddLoadingImages(){
        manager.load(loadingImages, TextureAtlas.class);
    }

    //----------Sounds----------

    public final String tictac = "sounds/tictac.mp3";
    public final String correct = "sounds/correct.mp3";
    public final String wrong = "sounds/wrong.mp3";

    public void queueAddSounds(){
        manager.load(tictac, Sound.class);
        manager.load(correct, Sound.class);
        manager.load(wrong, Sound.class);
    }

    //----------Music----------

    public final String playingSong = "music/theme.mp3";

    public void queueAddMusic(){
        manager.load(playingSong, Music.class);
    }

    //----------Skin----------

    //A Skin is libgxd’s way of keeping GUI elements like button textures, fonts and other GUI layout parameters all in one file.
    public final String skin = "skin/uiskin.json";

    public void queueAddSkin(){
        //For things with multiple files, we have to add a parameter.
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("skin/uiskin.atlas");
        manager.load(skin, Skin.class, params);
    }

    public void queueAddFonts(){
    }

    public void queueAddParticleEffects(){
    }



}
