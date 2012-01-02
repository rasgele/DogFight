package com.havzan.DogFight;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class AircraftDynamicsCalculator {
	private static final int MinSpeedPerSec = 0;
	private static final int MaxSpeedPerSec = 600;
	private static final float MAX_LEAN_PER_SEC = 180;
	private static final float MAX_PULL_PER_SEC = 20;
	private static Vector3 mTempVec3 = new Vector3();

	public void updateAircraft(Aircraft aircraft, float deltaTime){
		float stepLean = aircraft.mLean * MAX_LEAN_PER_SEC * deltaTime;
		float stepPull = aircraft.mPull * MAX_PULL_PER_SEC * deltaTime;

		Matrix4 combinedRotation = new Matrix4(aircraft.getCombinedMatrix());
		combinedRotation.getTranslation(mTempVec3);
		combinedRotation.trn(mTempVec3.tmp().mul(-1));

		Vector3 rollVector = new Vector3(1, 0, 0).mul(combinedRotation);

		Matrix4 rollMatrix = new Matrix4().setToRotation(rollVector, stepLean);

		rollMatrix.mul(combinedRotation);

		Vector3 pitchVector = new Vector3(0, 1, 0);
		pitchVector.mul(rollMatrix);
		Matrix4 pitchMatrix = new Matrix4();
		pitchMatrix.setToRotation(pitchVector, stepPull);

		pitchMatrix.mul(rollMatrix);

		Vector3 direction = new Vector3(1, 0, 0);
		direction.mul(pitchMatrix);
		direction.nor();
		aircraft.getDirection().set(direction);

		// Gdx.app.log(TAG, "deltaSec = " + deltaSec + ", m_speedPerSec = " +
		// m_speedPerSec + "   accele :" + mAcceleration);

		aircraft.mSpeedPerSec += aircraft.mAcceleration * deltaTime;
		aircraft.mSpeedPerSec = Math.max(MinSpeedPerSec,
				Math.min(MaxSpeedPerSec, aircraft.mSpeedPerSec));
		
		aircraft.mSpeedPerSec = aircraft.mThrust * MaxSpeedPerSec;
		
		aircraft.getLocation().add(direction.mul(aircraft.mSpeedPerSec* deltaTime));

		pitchMatrix.trn(aircraft.mLocation);
		aircraft.getCombinedMatrix().set(pitchMatrix);
	}
}
