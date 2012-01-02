package com.havzan.DogFight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Matrix4;

public class World {

	interface IWorldEventListener {
		void aircraftAdded(Aircraft aircraft);

		void missileAdded(Missile missile);

		void aircraftRemoved(Aircraft aircraft);

		void missileRemoved(Missile missile);
	}

	Terrain mTerrain;
	ArrayList<Aircraft> mAircrafts = new ArrayList<Aircraft>();
	ArrayList<Missile> mMissiles = new ArrayList<Missile>();
	Aircraft mPlayerAircraft;
	private boolean mMarkersEnabled = false;
	private Set<IWorldEventListener> mWorldEventListeners = new HashSet<World.IWorldEventListener>();
	HashMap<Aircraft, HashSet<Aircraft>> mTrackingList = new HashMap<Aircraft, HashSet<Aircraft>>();
	
	AircraftDynamicsCalculator mAircraftCalc = new AircraftDynamicsCalculator();

	public void create() {
		initializeWorld();
	}

	public void setWorldEventListener(IWorldEventListener listener) {
		mWorldEventListeners.add(listener);
	}

	private void addAircraft(Aircraft aircraft) {
		mAircrafts.add(aircraft);
		mTrackingList.put(aircraft, new HashSet<Aircraft>());
		for (IWorldEventListener listener : mWorldEventListeners)
			listener.aircraftAdded(aircraft);
	}

	private void addMissile(Missile missile) {
		mMissiles.add(missile);
		for (IWorldEventListener listener : mWorldEventListeners)
			listener.missileAdded(missile);
	}

	private void removeAircraft(Aircraft aircraft) {
		mAircrafts.remove(aircraft);
		mTrackingList.remove(aircraft);
		for (IWorldEventListener listener : mWorldEventListeners)
			listener.aircraftRemoved(aircraft);
	}

	private void removeMissile(Missile missile) {
		mMissiles.remove(missile);
		for (IWorldEventListener listener : mWorldEventListeners)
			listener.missileRemoved(missile);
	}

	private void initializeWorld() {
		mTerrain = new Terrain();
		mTerrain.create();

		mPlayerAircraft = new Aircraft();
		mPlayerAircraft.getLocation().set(0,0,2000);

		Aircraft drone = new Aircraft();
		drone.getLocation().set(0,0,2000);
		addAircraft(drone);
	}

	public void update(float delta) {
		mPlayerAircraft.update(delta);

		for (Aircraft a : mAircrafts)
			mAircraftCalc.updateAircraft(a, delta);//a.update(delta);

		for (Missile m : mMissiles)
			m.update(delta);

//		Collection<Aircraft> trackables = getTrackables(mPlayerAircraft);
//		HashSet<Aircraft> currentTrackables = mTrackingList.get(mPlayerAircraft);
//		for (Aircraft a : trackables)
//			currentTrackables.add(a);
	}

	public Collection<Aircraft> getTrackables(Aircraft tracker) {
		return TrackCalculator.calculate(tracker, mAircrafts);
	}

	public Aircraft getPlayer() {
		return mPlayerAircraft;
	}

	public boolean fireMissile(Aircraft aircraft) {
		Aircraft lockedObj = aircraft.getLocked();

		if (lockedObj != null) {
			Matrix4 misMtx = aircraft.getCombinedMatrix();

			Missile m = new Missile(misMtx).create();
			m.SetTarget(lockedObj);

			addMissile(m);

			return true;
		} else
			return false;
	}

	public boolean getMarkersEnabled() {
		// TODO Auto-generated method stub
		return mMarkersEnabled;
	}

	public void setMarkersEnabled(boolean enabled) {
		mMarkersEnabled = enabled;
	}

	public Collection<Aircraft> getAircrafts() {
		return mAircrafts;
	}

	public Collection<Missile> getMissiles() {
		return mMissiles;
	}

	public void setLocked(Aircraft tracker, Aircraft trackee) {
		tracker.setLocked(trackee);
	}
}
