package com.havzan.dogfight.game.physics;

import com.havzan.dogfight.game.physics.AircraftSolver;

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
