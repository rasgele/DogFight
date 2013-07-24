package com.havzan.DogFight.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

public class GameModel {
	private ArrayList<Aircraft> aircrafts = new ArrayList<Aircraft>();
	private ArrayList<Missile> missiles = new ArrayList<Missile>();
	public Terrain terrain;
	private Aircraft player;
	private HashSet<ModelObjectChangeLister> listeners = new HashSet<ModelObjectChangeLister>();
	
	public final static Vector3 Up = Vector3.Y;
	
	public List<Aircraft> getAircrafts(){
		return Collections.unmodifiableList(aircrafts);
	}
	public List<Missile> getMissiles(){
		return Collections.unmodifiableList(missiles);
	}
	
	public Aircraft addAircraft(Aircraft aircraft){
		aircrafts.add(aircraft);
		for (ModelObjectChangeLister l : listeners)
			l.aircraftAdded(aircraft);
		return aircraft;
	}
	
	public Missile addMissile(Missile missile){
		missiles.add(missile);
		for (ModelObjectChangeLister l : listeners)
			l.missileAdded(missile);
		return missile;
	}
	
	public Aircraft addPlayer(Aircraft aircraft){
		aircrafts.add(aircraft);
		player = aircraft;
		return aircraft;
	}
	
	public void removeAircraft(Aircraft aircraft){
		aircrafts.remove(aircraft);
		for (ModelObjectChangeLister l : listeners)
			l.aircraftRemoved(aircraft);
	}
	
	public void removeMissile(Missile missile){
		missiles.remove(missile);		
		for (ModelObjectChangeLister l : listeners)
			l.missileRemoved(missile);
	}

	public Aircraft getPlayer() {
		return player;
	}

	public Aircraft getAircraftByName(String name) {
		for (Aircraft a : aircrafts)
			if (a.getName().equals(name))
				return a;
		return null;
	}

	public Missile getMissileByName(String name) {
		for (Missile m : missiles)
			if (m.getName().equals(name))
				return m;
		return null;
	}	
	
	
	public void addListener(ModelObjectChangeLister listener){
		assert !listeners.contains(listener);
		listeners.add(listener);
	}
	public void removeListener(ModelObjectChangeLister listener){
		assert listeners.contains(listener);
		listeners.remove(listener);
	}
}
