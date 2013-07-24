package com.havzan.dogfight.game.model;

public interface ModelObjectChangeLister{
	public void missileAdded(Missile missile);
	public void aircraftAdded(Aircraft aircraft);
	//public void missileHit(Missile missile);
	public void aircraftRemoved(Aircraft aircraft);
	public void missileRemoved(Missile missile);
}