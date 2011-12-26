package com.havzan.DogFight;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DogFightDesktop {

	public static void main(String[] args) {
		new LwjglApplication(new DogFightGame(), "Game", 480, 320, false);
	}
}
