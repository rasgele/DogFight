package com.havzan.DogFight.game.physics;

import com.havzan.DogFight.game.model.Missile;

public interface MissileSolver {
	enum UpdateResult{
		MissileCruising,
		MissileLostContact,
		MissileHit
	}
	UpdateResult update(Missile missile, float deltaSecs);
}
