package com.havzan.dogfight.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.havzan.DogFight.DogFightGame;

public class DogFight extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(new DogFightGame(), false);
        graphics.getView().setKeepScreenOn(true);
    }
}