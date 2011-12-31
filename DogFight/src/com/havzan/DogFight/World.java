package com.havzan.DogFight;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Matrix4;

public class World {
	Terrain mTerrain;
	ArrayList<Aircraft> mAircrafts = new ArrayList<Aircraft>();
	ArrayList<Missile> mMissiles = new ArrayList<Missile>();
	Aircraft mUserAircraft;
	private boolean mMarkersEnabled = false;

	public void create(AssetManager assetManager) {
		initializeWorld(assetManager);
	}

	private void initializeWorld(AssetManager assetManager) {
		mTerrain = new Terrain();
		mTerrain.create(assetManager);

		mUserAircraft = new Aircraft().create(assetManager);

		Aircraft drone = new Aircraft().create(assetManager);
		mAircrafts.add(drone);
	}

	public void update(float delta) {
		mUserAircraft.update(delta);

		for (Aircraft a : mAircrafts)
			a.update(delta);

		for (Missile m : mMissiles)
			m.update(delta);
	}

	public Aircraft getPlayer() {
		return mUserAircraft;
	}

	public boolean fireMissile(Aircraft aircraft) {
		GameObject lockedObj = aircraft.getLocked();

		if (lockedObj != null) {
			Matrix4 misMtx = aircraft.getCombinedMatrix();

			Missile m = new Missile(misMtx).create();
			m.SetTarget(aircraft);
			
			mMissiles.add(m);

			return true;
		} else
			return false;
	}

	public boolean getMarkerseEnabled() {
		// TODO Auto-generated method stub
		return mMarkersEnabled;
	}

	public void setMarkersEnabled(boolean enabled) {
		mMarkersEnabled = enabled;
	}
}
