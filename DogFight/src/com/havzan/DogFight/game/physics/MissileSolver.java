package com.havzan.dogfight.game.physics;

import com.havzan.dogfight.game.model.Missile;

public interface MissileSolver {
	enum UpdateResult{
		MissileCruising,
		MissileLostContact,
		MissileHit
	}
	UpdateResult update(Missile missile, float deltaSecs);
}
