package com.havzan.DogFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Missile implements IWorldObject {
	private static final String TAG = "Aircraft";
	private static final float MaxSpeedPerSecond = 900;
	float mFlightTime;
	Mesh m_mesh;
	Texture m_texture;

	Vector3 mLocation;
	Vector3 mDirection;

	float m_speedPerSec = 0;
	private static float MaxRotationPerSecond = 90;
	private final double MaxTrackingAngle = Math.PI / 6;
	Matrix4 mCombinedMatrix;

	IWorldObject mTarget = null;
	boolean mIsTracking = false;

	public static float Range = 5000f;

	float mLastUpdate = 0.0f;
	static float mUpdateInterval = 0.00000f;

	public Missile(Matrix4 initPosition) {
		mCombinedMatrix = new Matrix4(initPosition);
		mLocation = new Vector3(mCombinedMatrix.val[Matrix4.M03],
				mCombinedMatrix.val[Matrix4.M13],
				mCombinedMatrix.val[Matrix4.M23]);

		mDirection = new Vector3(1, 0, 0).mul(
				mCombinedMatrix.cpy().trn(-mLocation.x, -mLocation.y,
						-mLocation.z)).nor();
	}

	public Missile create() {
		m_mesh = ObjLoader.loadObj(Gdx.files.internal("data/missile.obj")
				.read());
		Gdx.app.log("ObjTest", "obj bounds: " + m_mesh.calculateBoundingBox());
		m_texture = new Texture(Gdx.files.internal("data/camo.jpg"), true);
		m_texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		return this;
	}

	void SetTarget(Aircraft aircraft) {
		mTarget = aircraft;
		mIsTracking = true;
	}

	void update(float deltaSec) {
		if (!mIsTracking)
			return;

		mFlightTime += deltaSec;

		mLastUpdate += deltaSec;

		Vector3 targetLoc = mTarget.getLocation().cpy();
		Vector3 toTarget = targetLoc.sub(mLocation);
		Vector3 toTargetDir = toTarget.cpy().nor();
		float distanceToTarget = toTarget.len();

		if (mLastUpdate > mUpdateInterval) {
			double angleChanged = 0.0f;

			if (distanceToTarget > Range) {
				mIsTracking = false;
			}

			float angleToTarget = (float) getAngleBetween(mDirection,
					toTargetDir);

			float angleToTargetAbs = Math.abs(angleToTarget);
			float angleToTargetSign = Math.signum(angleToTarget);

			if (angleToTargetAbs > MaxTrackingAngle) {
				mIsTracking = false;
			}
			angleToTargetAbs = (float) (angleToTargetAbs * 180 / Math.PI);
			angleToTarget = (float) (angleToTarget * 180 / Math.PI);

			if (angleToTarget != 0 && mIsTracking) {
				Vector3 rotationAxis = mDirection.cpy();
				rotationAxis.crs(toTargetDir);
				rotationAxis.nor();

				float rotationAmount = MaxRotationPerSecond;

				if (rotationAmount * deltaSec > angleToTargetAbs) {
					rotationAmount = angleToTargetAbs / deltaSec;
					// rotationAmount *= 1.2;
				}

				rotationAmount *= angleToTargetSign;

				float deltaRotation = rotationAmount * deltaSec;

				Matrix4 rotationMtx = new Matrix4();
				rotationMtx.setToRotation(rotationAxis, deltaRotation);

				Matrix4 combinedRotation = new Matrix4(mCombinedMatrix);
				combinedRotation.trn(-mCombinedMatrix.val[Matrix4.M03],
						-mCombinedMatrix.val[Matrix4.M13],
						-mCombinedMatrix.val[Matrix4.M23]);

				rotationMtx.mul(combinedRotation);

				Vector3 direction = new Vector3(1, 0, 0);
				direction.mul(rotationMtx);
				direction.nor();

				angleChanged = (float) getAngleBetween(mDirection, direction);
				angleChanged = (float) (angleChanged * 180 / Math.PI);

				double newAngleToTarget = getAngleBetween(direction,
						toTargetDir);
				newAngleToTarget = (newAngleToTarget * 180 / Math.PI);
				double rateOfChange = angleChanged / deltaSec;

				Gdx.app.log(TAG, "Old: " + angleToTarget + " New: "
						+ newAngleToTarget + " Changed : " + angleChanged
						+ " RoC : " + rateOfChange + "Distance :" + distanceToTarget);

				mDirection.set(direction);

				mCombinedMatrix.set(rotationMtx);

			}
			mLastUpdate = 0;
		}

		if (mFlightTime > 3.0)
			m_speedPerSec = MaxSpeedPerSecond;
		else {
			m_speedPerSec = MaxSpeedPerSecond / (4.0f - mFlightTime);
		}
		if (mIsTracking) {
			Vector3 deltaPos = mDirection.cpy().mul(m_speedPerSec * deltaSec);

			mLocation.add(deltaPos);
			mCombinedMatrix.trn(mLocation);
		}
		if (mIsTracking && distanceToTarget < 10) {
			Gdx.app.log(TAG, "HIT!!!!!!!!!!!!!");
			mIsTracking = false;
		} else if (!mIsTracking)
			Gdx.app.log(TAG, "Missed");

	}

	void Render() {
		// Gdx.app.log(TAG, "Pos :" + m_location);
		GL10 gl = Gdx.graphics.getGL10();
		gl.glPushMatrix();

		gl.glMultMatrixf(mCombinedMatrix.val, 0);

		m_texture.bind();

		m_mesh.render(GL10.GL_TRIANGLES);

		gl.glPopMatrix();
	}

	double getAngleBetween(Vector3 vec1, Vector3 vec2) {
		float dot = vec1.dot(vec2);
		return Math.acos(Math.min(1.0, dot));
	}

	public Vector3 getLocation() {
		return mLocation.cpy();
	}

	public Vector3 getDirection() {
		return mDirection.cpy();
	}

	public Matrix4 getCombinedMatrix() {
		return mCombinedMatrix;
	}
}
