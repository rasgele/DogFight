package com.havzan.dogfight;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.havzan.DogFight.DogFightGame;

import android.app.Activity;
import android.os.Bundle;

public class DogFight extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(new DogFightGame(), false);
    }
}