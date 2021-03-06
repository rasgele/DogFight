package com.havzan.dogfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.havzan.dogfight.WorldRenderer.IWorldPresenter;

public class WorldPresenter implements IWorldPresenter {

	private World mWorld;
	private WorldRenderer mWorldRenderer;

	public WorldPresenter(World world) {
		TerrainGenerator mTerrainGenerator = new TerrainGenerator(20000, 5000, 128);
		Model terrainMesh = mTerrainGenerator.createMesh();
		Assets.setTerrainModel(terrainMesh);
		mWorld = world;
		mWorldRenderer = new WorldRenderer(this, mWorld,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mWorldRenderer.create();
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
		mWorld.setMarkersEnabled(!mWorld.getMarkersEnabled());
	}

	public void render(float delta) {
		mWorldRenderer.updateControls();
		mWorld.update(delta);
		mWorldRenderer.render(delta);
	}

}
