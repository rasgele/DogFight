package com.havzan.dogfight.game.physics;

import com.havzan.dogfight.game.model.Aircraft;

public interface AircraftSolver {
	public void update(Aircraft aircraft, float deltaSecs);
}
