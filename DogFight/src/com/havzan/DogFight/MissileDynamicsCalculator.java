package com.havzan.dogfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MissileDynamicsCalculator {
	private static final float Range = 2000;
	private static final float MaxTrackingAngle = (float) (Math.PI / 4);
	private static final float MaxRotationPerSecond = 90;
	private static final String TAG = "MISSILE DYN";
	private static final float MaxSpeedPerSecond = 900;

	public static interface IMissileDynamicsListener {
		void onMissileHit(Missile m);

		void onMissileMissed(Missile m);
	}

	private IMissileDynamicsListener mListener = null;

	public void setListener(IMissileDynamicsListener listener) {
		mListener = listener;
	}

	void updateMissile(Missile missile, float delta) {
		if (!missile.mIsTracking)
			return;

		missile.mFlightTime += delta;
		missile.mLastUpdate += delta;

		Vector3 targetLoc = missile.mTarget.getLocation().cpy();
		Vector3 toTarget = targetLoc.sub(missile.getLocation());
		Vector3 toTargetDir = toTarget.cpy().nor();
		float distanceToTarget = toTarget.len();

		if (missile.mLastUpdate > Missile.mUpdateInterval) {
			double angleChanged = 0.0f;

			if (distanceToTarget > Range) {
				missile.mIsTracking = false;
			}

			float angleToTarget = (float) MathUtil.getAngleBetween(
					missile.mDirection, toTargetDir);

			float angleToTargetAbs = Math.abs(angleToTarget);
			float angleToTargetSign = Math.signum(angleToTarget);

			if (angleToTargetAbs > MaxTrackingAngle) {
				missile.mIsTracking = false;
			}
			angleToTargetAbs = (float) (angleToTargetAbs * 180 / Math.PI);
			angleToTarget = (float) (angleToTarget * 180 / Math.PI);

			if (angleToTarget != 0 && missile.mIsTracking) {
				Vector3 rotationAxis = missile.mDirection.cpy();
				rotationAxis.crs(toTargetDir);
				rotationAxis.nor();

				float rotationAmount = MaxRotationPerSecond;

				if (rotationAmount * delta > angleToTargetAbs) {
					rotationAmount = angleToTargetAbs / delta;
					// rotationAmount *= 1.2;
				}

				rotationAmount *= angleToTargetSign;

				float deltaRotation = rotationAmount * delta;

				Matrix4 rotationMtx = new Matrix4();
				rotationMtx.setToRotation(rotationAxis, deltaRotation);

				Matrix4 combinedRotation = new Matrix4(missile.mCombinedMatrix);
				combinedRotation.trn(-missile.mCombinedMatrix.val[Matrix4.M03],
						-missile.mCombinedMatrix.val[Matrix4.M13],
						-missile.mCombinedMatrix.val[Matrix4.M23]);

				rotationMtx.mul(combinedRotation);

				Vector3 direction = new Vector3(1, 0, 0);
				direction.mul(rotationMtx);
				direction.nor();

				angleChanged = (float) MathUtil.getAngleBetween(
						missile.mDirection, direction);
				angleChanged = (float) (angleChanged * 180 / Math.PI);

				double newAngleToTarget = MathUtil.getAngleBetween(direction,
						toTargetDir);
				newAngleToTarget = (newAngleToTarget * 180 / Math.PI);
				double rateOfChange = angleChanged / delta;

				Gdx.app.log(TAG, "Old: " + angleToTarget + " New: "
						+ newAngleToTarget + " Changed : " + angleChanged
						+ " RoC : " + rateOfChange + "Distance :"
						+ distanceToTarget);

				missile.mDirection.set(direction);

				missile.mCombinedMatrix.set(rotationMtx);

			}
			missile.mLastUpdate = 0;
		}

		if (missile.mFlightTime > 3.0)
			missile.m_speedPerSec = MaxSpeedPerSecond;
		else {
			missile.m_speedPerSec = MaxSpeedPerSecond
					/ (4.0f - missile.mFlightTime);
		}
		if (missile.mIsTracking) {
			Vector3 deltaPos = missile.mDirection.tmp().mul(
					missile.m_speedPerSec * delta);

			missile.mLocation.add(deltaPos);
			missile.mCombinedMatrix.trn(missile.mLocation);
		}
		if (missile.mIsTracking && distanceToTarget < 10) {
			Gdx.app.log(TAG, "HIT!!!!!!!!!!!!!");
			missile.mIsTracking = false;
			if (mListener != null) {
				mListener.onMissileHit(missile);
			}
		} else if (!missile.mIsTracking) {
			Gdx.app.log(TAG, "Missed");
			if (mListener != null) {
				mListener.onMissileMissed(missile);
			}
		}
	}
}
