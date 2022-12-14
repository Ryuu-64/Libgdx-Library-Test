package org.ryuu.gdx.test;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.ryuu.gdx.GameApplication;

import static org.ryuu.gdx.GameApplication.GAME_APPLICATION;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(GAME_APPLICATION, config);
    }
}