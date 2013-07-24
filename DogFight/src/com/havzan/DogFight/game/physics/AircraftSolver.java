package com.havzan.DogFight.game.physics;

import com.havzan.DogFight.game.model.Aircraft;

public interface AircraftSolver {
	public void update(Aircraft aircraft, float deltaSecs);
}
