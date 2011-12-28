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
	private static final float MaxSpeedPerSecond = 600;
	private float mFlightTime;
	Mesh m_mesh;
	Texture m_texture;

	Vector3 mLocation;
	private Vector3 mDirection;

	private float m_speedPerSec = 0;
	private static float MaxRotationPerSecond = 135;
	private Matrix4 m_combinedMatrix;

	private IWorldObject m_target = null;
	private boolean m_tracking = false;

	public static float Range = 5000f;

	private float mLastUpdate = 0.0f;
	private static float mUpdateInterval = 0.00003f;

	public Missile(Matrix4 initPosition) {
		m_combinedMatrix = new Matrix4(initPosition);
		mLocation = new Vector3(m_combinedMatrix.val[Matrix4.M03],
				m_combinedMatrix.val[Matrix4.M13],
				m_combinedMatrix.val[Matrix4.M23]);

		mDirection = new Vector3(1, 0, 0).mul(
				m_combinedMatrix.cpy().trn(-mLocation.x, -mLocation.y,
						-mLocation.z)).nor();
	}

	void Create() {
		m_mesh = ObjLoader.loadObj(Gdx.files.internal("data/missile.obj")
				.read());
		Gdx.app.log("ObjTest", "obj bounds: " + m_mesh.calculateBoundingBox());
		m_texture = new Texture(Gdx.files.internal("data/camo.jpg"), true);
		m_texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
	}

	void SetTarget(Aircraft aircraft) {
		m_target = aircraft;
		m_tracking = true;
	}

	void Update(float deltaSec) {
		if (!m_tracking)
			return;
		mFlightTime += deltaSec;
		
		mLastUpdate += deltaSec;

		Vector3 targetLoc = m_target.getLocation().cpy();
		Vector3 toTarget = targetLoc.sub(mLocation);
		Vector3 toTargetDir = toTarget.cpy().nor();
		float distanceToTarget = toTarget.len();

		if (mLastUpdate > mUpdateInterval) {
			double angleChanged = 0.0f;

			if (distanceToTarget > Range) {
				m_tracking = false;
			}

			float angleToTarget = (float) getAngleBetween(mDirection,
					toTargetDir);

			float angleToTargetAbs = Math.abs(angleToTarget);
			float angleToTargetSign = Math.signum(angleToTarget);

			if (angleToTargetAbs > Math.PI / 3) {
				m_tracking = false;
			}

			angleToTarget = (float) (angleToTarget * 180 / Math.PI);

			if (angleToTarget != 0 && m_tracking) {
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

				Matrix4 combinedRotation = new Matrix4(m_combinedMatrix);
				combinedRotation.trn(-m_combinedMatrix.val[Matrix4.M03],
						-m_combinedMatrix.val[Matrix4.M13],
						-m_combinedMatrix.val[Matrix4.M23]);

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
						+ " RoC : " + rateOfChange + " DeltaRot :"
						+ deltaRotation + " DR: " + deltaRotation / deltaSec);

				mDirection.set(direction);

				m_combinedMatrix.set(rotationMtx);

			}
			mLastUpdate = 0;
		}

		if (mFlightTime > 8.0)
			m_speedPerSec = MaxSpeedPerSecond;
		else{
			m_speedPerSec = MaxSpeedPerSecond / (9.0f - mFlightTime);
		}
		Gdx.app.log(TAG, "Speed per sec :" + m_speedPerSec);
		Vector3 deltaPos = mDirection.cpy().mul(m_speedPerSec * deltaSec);
		mLocation.add(deltaPos);
		m_combinedMatrix.trn(deltaPos);

		if (m_tracking && distanceToTarget < 10) {
			Gdx.app.log(TAG, "HIT!!!!!!!!!!!!!");
			m_tracking = false;
		}
		else if (!m_tracking)
			Gdx.app.log(TAG, "Missed");

	}

	void Render() {
		// Gdx.app.log(TAG, "Pos :" + m_location);
		GL10 gl = Gdx.graphics.getGL10();
		gl.glPushMatrix();

		gl.glMultMatrixf(m_combinedMatrix.val, 0);

		 //gl.glDisable(GL10.GL_TEXTURE);

		m_texture.bind();

		//gl.glColor4f(1, 0, 0, 1);
		m_mesh.render(GL10.GL_TRIANGLES);

		//gl.glColor4f(1, 1, 1, 0);

		//gl.glEnable(GL10.GL_TEXTURE);

		gl.glPopMatrix();
	}

	double getAngleBetween(Vector3 vec1, Vector3 vec2) {
		float dot = vec1.dot(vec2);
		return Math.acos(Math.min(1.0, dot));
	}

	public Vector3 getLocation() {

		return mLocation;
	}

	public Vector3 getDirection() {
		// TODO Auto-generated method stub
		return mDirection;
	}
}
