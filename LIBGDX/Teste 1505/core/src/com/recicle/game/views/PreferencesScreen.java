package com.recicle.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.recicle.game.MainClass;

public class PreferencesScreen implements Screen {

    private MainClass parent; // a field to store our orchestrator
    private Stage stage;

    private Label titleLabel;
    private Label musicVolumeLabel;
    private Label soundVolumeLabel;
    private Label musicCheckBoxLabel;
    private Label soundCheckBoxLabel;

    private Skin skin;


    public PreferencesScreen(MainClass mainClass) {
        parent = mainClass;

        stage = new Stage(new ScreenViewport()); //This is the controller and will react to inputs from the user. In our constructor
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        skin = parent.assetsManager.manager.get("skin/glassy-ui.json");
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);

        stage.addActor(table);

        //Enable music
        final CheckBox musicCheckbox = new CheckBox(null, skin);
        musicCheckbox.setChecked( parent.getPreferences().isMusicEnabled() );
        musicCheckbox.addListener( new EventListener() {

            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                parent.getPreferences().setMusicEnabled( enabled );
                return false;
            }
        });

        //Music volume
        final Slider musicVolumeSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        musicVolumeSlider.setValue( parent.getPreferences().getMusicVolume() );
        musicVolumeSlider.addListener( new EventListener() {

            @Override
            public boolean handle(Event event) {
                parent.getPreferences().setMusicVolume( musicVolumeSlider.getValue() );
                return false;
            }
        });

        //Enable sound
        final CheckBox soundCheckBox = new CheckBox(null, skin);
        soundCheckBox.setChecked( parent.getPreferences().isSoundEnabled() );
        soundCheckBox.addListener( new EventListener() {

            @Override
            public boolean handle(Event event) {
                boolean enabled = soundCheckBox.isChecked();
                parent.getPreferences().setSoundEnabled( enabled );
                return false;
            }
        });

        //Sound volume
        final Slider soundVolumeSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        soundVolumeSlider.setValue( parent.getPreferences().getSoundVolume() );
        soundVolumeSlider.addListener( new EventListener() {

            @Override
            public boolean handle(Event event) {
                parent.getPreferences().setSoundVolume( soundVolumeSlider.getValue() );
                return false;
            }
        });


        //Return to Menu
        final TextButton backButton = new TextButton("Back", skin, "small"); // the extra argument here "small" is used to set the button to the smaller version instead of the big default version
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(MainClass.MENU);
            }
        });

        titleLabel = new Label( "Preferences", skin );
        musicVolumeLabel = new Label( "Music Volume", skin );
        soundVolumeLabel = new Label( "Sound Volume", skin );
        musicCheckBoxLabel = new Label( "Enable Music", skin );
        soundCheckBoxLabel = new Label( "Enable Sound", skin );

        table.add(titleLabel).colspan(2);

        table.row().pad(10);
            table.add(musicVolumeLabel).left();
            table.add(musicVolumeSlider);

//        table.row().pad(10);
//            table.add(soundVolumeLabel).left();
//            table.add(soundVolumeSlider);

        table.row().pad(10);
            table.add(musicCheckBoxLabel).left();
            table.add(musicCheckbox);

        table.row().pad(10);
            table.add(soundCheckBoxLabel).left();
            table.add(soundCheckBox);

        table.row().pad(10);
            table.add(backButton).colspan(2);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        MenuScreen.themeSong.setVolume(parent.preferences.getMusicVolume());
        if (parent.preferences.isMusicEnabled() == false) MenuScreen.themeSong.pause();
        else MenuScreen.themeSong.play();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();

    }
}
