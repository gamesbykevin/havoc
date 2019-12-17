package com.gamesbykevin.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.gamesbykevin.havoc.MyGdxGame;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;

public class HtmlLauncher extends GwtApplication {

        // USE THIS CODE FOR A FIXED SIZE APPLICATION
        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(SIZE_WIDTH, SIZE_HEIGHT);
        }
        // END CODE FOR FIXED SIZE APPLICATION

        @Override
        public ApplicationListener createApplicationListener () {
                return new MyGdxGame();
        }
}