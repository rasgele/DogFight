package com.havzan.dogfight.game.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.havzan.dogfight.MathUtil;
import com.havzan.dogfight.game.model.Missile;

public class MissileDynamicsCalculator implements MissileSolver {
	private static final float Range = 2000;
	private static final float MaxTrackingAngle = (float) (Math.PI / 4);
	private static final float MaxRotationPerSecond = 45;
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

	UpdateResult updateMissile(Missile missile, float delta) {
		if (!missile.getIsTracking())
			return UpdateResult.MissileLostContact;
		
		updateMissileLocation(missile, delta);

		Vector3 toTarget = missile.getVectorToTarget(new Vector3());
		Vector3 toTargetDir = toTarget.cpy().nor();
		
//		Gdx.app.log(TAG, "VtT : " + toTargetDir);
		

		float distanceToTarget = toTarget.len();

		if (distanceToTarget > Range) {
			missile.setIsTracking(false);
			return UpdateResult.MissileLostContact;
		}		

		Vector3 missileDirection = new Vector3();
		float angleToTarget = (float) MathUtil.getAngleBetween(
				missile.getDirection(missileDirection), toTargetDir);

		float angleToTargetAbs = Math.abs(angleToTarget);
		float angleToTargetSign = Math.signum(angleToTarget);
		
//		Gdx.app.log(TAG, "AtT :" + angleToTarget);

		if (angleToTargetAbs > MaxTrackingAngle) {
			missile.setIsTracking(false);
			return UpdateResult.MissileLostContact;
		}

		angleToTargetAbs = (float) (angleToTargetAbs * 180 / Math.PI);
		angleToTarget = (float) (angleToTarget * 180 / Math.PI);

		if (angleToTarget != 0 && missile.getIsTracking()) {
			Vector3 rotationAxis = missileDirection;
			rotationAxis.crs(toTargetDir);
			rotationAxis.nor();

			float rotationAmount = Math.min(angleToTargetAbs,
					MaxRotationPerSecond * delta);

			rotationAmount *= angleToTargetSign;

			Matrix4 rotationMtx = new Matrix4();
			rotationMtx.setToRotation(rotationAxis, rotationAmount);

			Matrix4 combinedRotation = new Matrix4(missile.getOrientation());
			Vector3 initialPosition = missile.getLocation(new Vector3());
			
			combinedRotation.trn(-combinedRotation.val[Matrix4.M03],
					-combinedRotation.val[Matrix4.M13],
					-combinedRotation.val[Matrix4.M23]);

			rotationMtx.mul(combinedRotation);
			
			rotationMtx.setTranslation(initialPosition);			

			missile.setOrientation(rotationMtx);
			
			// angleChanged = (float) MathUtil.getAngleBetween(
			// missile.mDirection, direction);
			// angleChanged = (float) (angleChanged * 180 / Math.PI);
			//
			// double newAngleToTarget = MathUtil.getAngleBetween(direction,
			// toTargetDir);
			// newAngleToTarget = (newAngleToTarget * 180 / Math.PI);
			// double rateOfChange = angleChanged / delta;
			//
			// Gdx.app.log(TAG, "Old: " + angleToTarget + " New: "
			// + newAngleToTarget + " Changed : " + angleChanged
			// + " RoC : " + rateOfChange + "Distance :"
			// + distanceToTarget);


		}
		// }

		missile.speed = Math.min(missile.params.MaxSpeedPerSec, missile.speed
				+ missile.acceleration * delta);

		if (missile.getIsTracking() && distanceToTarget < 10) {
			Gdx.app.log(TAG, "HIT!!!!!!!!!!!!!");
			missile.setIsTracking(false);
			if (mListener != null) {
				mListener.onMissileHit(missile);
			}
			return UpdateResult.MissileHit;
			
		} else if (!missile.getIsTracking()) {
			Gdx.app.log(TAG, "Missed");
			if (mListener != null) {
				mListener.onMissileMissed(missile);
			}
			return UpdateResult.MissileLostContact;
		}
		return UpdateResult.MissileCruising;
	}

	private void updateMissileLocation(Missile missile, float delta) {
		Vector3 direction = missile.getDirection(new Vector3());
		Vector3 location = missile.getLocation(new Vector3());
		direction.scl(missile.speed * delta).add(location);

		Matrix4 orientation = new Matrix4(missile.getOrientation());
		orientation.setTranslation(direction);

		missile.setOrientation(orientation);
	}

	@Override
	public UpdateResult update(Missile missile, float deltaSecs) {
		return updateMissile(missile, deltaSecs);

	}
}
