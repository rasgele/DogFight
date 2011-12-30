package com.havzan.DogFight;

import com.badlogic.gdx.Screen;

public class MainGameScreen implements Screen {

	DogFightGame mGame;
	World mWorld;
	
	public MainGameScreen(DogFightGame game) {
		mGame = game;
		mWorld = new World();
		mWorld.create(mGame.mAssetManager);
	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
