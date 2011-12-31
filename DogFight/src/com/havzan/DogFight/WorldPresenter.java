package com.havzan.DogFight;

import com.badlogic.gdx.Gdx;
import com.havzan.DogFight.WorldRenderer.IWorldPresenter;

public class WorldPresenter implements IWorldPresenter {

	private World mWorld;
	private WorldRenderer mWorldRenderer;

	public WorldPresenter() {
		mWorld = new World();
		mWorldRenderer = new WorldRenderer(this, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void onMissileFire() {
		Aircraft player = mWorld.getPlayer();
		mWorld.fireMissile(player);
	}

	@Override
	public void onCameraSwitch() {
	}

	@Override
	public void onMarkerToggle() {
		mWorld.setMarkersEnabled(!mWorld.getMarkerseEnabled());
	}

}
