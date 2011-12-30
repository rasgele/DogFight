package com.havzan.DogFight;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;

public class World {
	Terrain mTerrain;
	ArrayList<Aircraft> mAircrafts = new ArrayList<Aircraft>();
	ArrayList<Missile> mMissiles = new ArrayList<Missile>();
	Aircraft mUserAircraft;
	
	public void create(AssetManager assetManager){
		initializeWorld(assetManager);
	}
	
	private void initializeWorld(AssetManager assetManager) {
		mTerrain = new Terrain();
		mTerrain.create(assetManager);
		
		mUserAircraft = new Aircraft().create(assetManager);
		
		Aircraft drone = new Aircraft().create(assetManager);
		mAircrafts.add(drone);
	}
	
	public void update(float delta){
		mUserAircraft.update(delta);
		
		for (Aircraft a : mAircrafts)
			a.update(delta);
		
		for (Missile m : mMissiles)
			m.update(delta);
	}
}
