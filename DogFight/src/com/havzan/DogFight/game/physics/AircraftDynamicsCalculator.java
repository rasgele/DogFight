package com.havzan.dogfight.game.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.havzan.dogfight.game.model.Aircraft;
import com.havzan.dogfight.game.model.AircraftParams;

public class AircraftDynamicsCalculator implements AircraftSolver {
	private static Vector3 mTempVec3 = new Vector3();
//
	public void updateAircraft(Aircraft aircraft, float deltaTime){
		final AircraftParams param = aircraft.params;
		float stepLean = aircraft.getStickX() * param.MaxRollRatePerSec * deltaTime;
		float stepPull = aircraft.getStickY() * param.MaxPitchRatePerSec * deltaTime;

		// Undo translation
		Matrix4 combinedRotation = new Matrix4(aircraft.getOrientation());
		combinedRotation.getTranslation(mTempVec3);
		combinedRotation.trn(mTempVec3.scl(-1));
		
		// Get current direction
		Vector3 rollVector = new Vector3();
		aircraft.getDirection(rollVector);
		// Roll around direction vector
		Matrix4 rollMatrix = new Matrix4().setToRotation(rollVector, stepLean);
		rollMatrix.mul(combinedRotation);
		
		// Calculate pitch vector (right vector?)
		Vector3 pitchVector = new Vector3();
		aircraft.getInitRight(pitchVector);
		pitchVector.mul(rollMatrix);
		
		// calculate pitch rotation matrix
		Matrix4 pitchMatrix = new Matrix4().setToRotation(pitchVector, stepPull);		
		// combine roll and pitch
		pitchMatrix.mul(rollMatrix);
		
		// Calculate new direction
		Vector3 direction = new Vector3();
		aircraft.getInitDirection(direction);
		direction.mul(pitchMatrix);
		direction.nor();

		// Gdx.app.log(TAG, "deltaSec = " + deltaSec + ", m_speedPerSec = " +
		// m_speedPerSec + "   accele :" + mAcceleration);

		aircraft.speed += aircraft.acceleration * deltaTime;
		aircraft.speed = Math.max(param.MinSpeedPerSec,
				Math.min(param.MaxSpeedPerSec, aircraft.speed));
		
		// TODO remove this debug case
		aircraft.speed = aircraft.getThrust() * param.MaxSpeedPerSec;
		
		// Calculate new displacement
		direction.scl(aircraft.speed * deltaTime);
		// Add old translation
		direction.sub(mTempVec3);
		
		//aircraft.getLocation().add(direction.mul(aircraft.speed* deltaTime));

		pitchMatrix.trn(direction);
		aircraft.setOrientation(pitchMatrix);
	}

	@Override
	public void update(Aircraft aircraft, float deltaSecs) {
		updateAircraft(aircraft, deltaSecs);
		
	}
}
