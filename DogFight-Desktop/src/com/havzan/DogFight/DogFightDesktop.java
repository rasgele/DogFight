package com.havzan.DogFight;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.havzan.DogFight.DogFightGame;

public class DogFightDesktop {

	public static void main(String[] args) {
		new LwjglApplication(new DogFightGame(), "Game", 640, 480, false);
	}
}
