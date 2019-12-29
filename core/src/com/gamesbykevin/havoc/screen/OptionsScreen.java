package com.gamesbykevin.havoc.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.havoc.MyGdxGame;
import com.gamesbykevin.havoc.exception.ScreenException;
import com.gamesbykevin.havoc.preferences.AppPreferences;
import com.gamesbykevin.havoc.util.Language;

import static com.gamesbykevin.havoc.preferences.AppPreferences.*;
import static com.gamesbykevin.havoc.screen.ScreenHelper.SCREEN_MENU;
import static com.gamesbykevin.havoc.util.Language.changeMyBundle;
import static com.gamesbykevin.havoc.util.Language.getMyBundle;

public class OptionsScreen extends TemplateScreen {

    public OptionsScreen(MyGdxGame game) {
        super(game);
    }

    @Override
    public void show() {

        super.show();

        //capture the menu input
        captureInput();

        TextButton buttonMusic = new TextButton(getMyBundle().get("optionsScreenMusic"), getSkin(), "toggle");
        buttonMusic.setChecked(hasEnabledMusic());

        //Add listeners to buttons
        buttonMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPreferenceMusic(buttonMusic.isChecked());
            }
        });

        TextButton buttonSound = new TextButton(getMyBundle().get("optionsScreenSound"), getSkin(), "toggle");
        buttonSound.setChecked(hasEnabledSfx());

        //Add listeners to buttons
        buttonSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPreferenceSound(buttonSound.isChecked());
            }
        });

        TextButton buttonVibrate = new TextButton(getMyBundle().get("optionsScreenVibrate"), getSkin(), "toggle");
        buttonVibrate.setChecked(hasEnabledVibrate());

        //Add listeners to buttons
        buttonVibrate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPreferenceVibrate(buttonVibrate.isChecked());
            }
        });

        //create our back button
        TextButton buttonBack = new TextButton(getMyBundle().get("optionsScreenBack"), getSkin());

        //Add listeners to buttons
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    getGame().getScreenHelper().changeScreen(SCREEN_MENU);
                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Create Table
        Table table = new Table();

        //Set table to fill stage
        table.setFillParent(true);

        //Set alignment of contents in the table.
        table.center();

        table.add(buttonMusic).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.add(buttonSound).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);

        //only mobile phones can vibrate
        switch(Gdx.app.getType()) {

            case iOS:
            case Android:
            default:
                table.add(buttonVibrate).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
                break;
        }

        //html, web, desktop will select language and have a back button, this is built into android
        switch(Gdx.app.getType()) {

            case WebGL:
            case Applet:
            case Desktop:
            case HeadlessDesktop:
                Label labelLanguage = new Label("Language", getSkin());
                final Language.Languages[] languages = Language.Languages.values();

                int languageIndexDefault = 0;

                String[] items = new String[languages.length];
                for (int i = 0; i < languages.length; i++) {
                    items[i] = languages[i].getDesc();

                    //default language to english
                    if (languages[i].getLanguageCode().equalsIgnoreCase("EN"))
                        languageIndexDefault = i;
                }

                //create our drop down
                final SelectBox<String> dropdown = new SelectBox<>(getSkin());

                //add our item selections
                dropdown.setItems(items);

                //pre select the value if it exists
                final int languageIndex = getPreferenceValue(AppPreferences.PREF_LANGUAGE);
                if (languageIndex >= 0) {
                    dropdown.setSelectedIndex(languageIndex);
                } else {
                    dropdown.setSelectedIndex(languageIndexDefault);
                }

                dropdown.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {

                        //change the language
                        changeMyBundle(dropdown.getSelectedIndex());

                        //store language in the preferences
                        setPreference(AppPreferences.PREF_LANGUAGE, dropdown.getSelectedIndex());
                    }
                });

                //create the language ui in a separate table
                Table child = new Table();
                child.add(labelLanguage).pad(BUTTON_PADDING);
                child.add(dropdown).pad(BUTTON_PADDING);

                //now add the language table to the parent as well as the back button
                table.row();
                table.add(buttonBack).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
                table.add(child).colspan(2).left();
                break;
        }

        //Add table to stage
        getStage().clear();
        getStage().addActor(table);

        //add our social media icons
        super.addSocialIcons();
    }

    @Override
    public void render(float delta) {

        //clear the screen
        super.clearScreen();

        //act the stage
        getStage().act();

        //now render
        getStage().getBatch().begin();
        super.drawBackground(getStage().getBatch());
        super.drawLogo(getStage().getBatch());
        getStage().getBatch().end();

        //draw the remaining of the stage
        getStage().draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}