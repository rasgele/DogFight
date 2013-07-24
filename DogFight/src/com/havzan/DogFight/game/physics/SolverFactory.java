package com.havzan.DogFight.game.physics;

import com.havzan.DogFight.game.physics.AircraftSolver;

public class SolverFactory {

	public static AircraftSolver getAircraftSolver(String typeId) {
		// TODO Auto-generated method stub
		return new AircraftDynamicsCalculator();
	}
	
	public static MissileSolver getMissileSolver(String typeId) {
		// TODO Auto-generated method stub
		return new MissileDynamicsCalculator();
	}
}
