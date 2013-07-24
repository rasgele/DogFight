package com.havzan.DogFight;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.havzan.dogfight.DogFightGame;

public class DogFightDesktop {

	public static void main(String[] args) {
		new LwjglApplication(new DogFightGame(), "Game", 640, 480, false);
	}
}
