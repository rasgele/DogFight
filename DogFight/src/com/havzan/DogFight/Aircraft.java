package com.havzan.DogFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Aircraft implements IWorldObject {
	private static final String TAG = "Aircraft";
	Mesh mMesh;
	Texture mTexture;
	Vector3 mLocation = new Vector3();
	private Vector3 mDirection = new Vector3(0, 0, -1);

	private float m_lean = 0;
	private float m_pull = 0;
	private float m_speedPerSec = 0;
	private float m_maxLeanPerSec = 80;
	private float m_maxPullPerSec = 20;
	private Matrix4 m_combinedMatrix = new Matrix4();
	private float mThrust = 0;
	private float mAcceleration = 0;

	private static float MaxSpeedPerSec = 400;
	private static float MinSpeedPerSec = 0;

	Aircraft() {
	}

	Aircraft(float lean, float pull) {
		Matrix4 combinedRotation = new Matrix4(m_combinedMatrix);
		
		Vector3 rollVector = new Vector3(1, 0, 0).mul(combinedRotation);

		Matrix4 rollMatrix = new Matrix4().setToRotation(rollVector, lean);

		rollMatrix.mul(combinedRotation);

		Vector3 pitchVector = new Vector3(0, 1, 0);
		pitchVector.mul(rollMatrix);
		
		Matrix4 pitchMatrix = new Matrix4();
		pitchMatrix.setToRotation(pitchVector, pull);

		pitchMatrix.mul(rollMatrix);
		
		m_combinedMatrix = pitchMatrix;
	}

	public Aircraft create(AssetManager mAssetManager) {
		mMesh = ObjLoader.loadObj(Gdx.files.internal("data/plane.obj")
				.read());
		Gdx.app.log("ObjTest", "obj bounds: " + mMesh.calculateBoundingBox());
		
		mTexture = mAssetManager.get("data/camo.jpg", Texture.class);
		mTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		
		return this;
		
	}
	void Create() {
		mMesh = ObjLoader.loadObj(Gdx.files.internal("data/plane.obj")
				.read());
		Gdx.app.log("ObjTest", "obj bounds: " + mMesh.calculateBoundingBox());
		mTexture = new Texture(Gdx.files.internal("data/camo.jpg"), true);
		mTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
	}

	void update(float deltaSec) {
		float stepLean = m_lean * m_maxLeanPerSec * deltaSec;
		float stepPull = m_pull * m_maxPullPerSec * deltaSec;

		Matrix4 combinedRotation = new Matrix4(m_combinedMatrix);
		combinedRotation.trn(-m_combinedMatrix.val[Matrix4.M03],
				-m_combinedMatrix.val[Matrix4.M13],
				-m_combinedMatrix.val[Matrix4.M23]);

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
		mDirection = new Vector3(direction);

		// Gdx.app.log(TAG, "deltaSec = " + deltaSec + ", m_speedPerSec = " +
		// m_speedPerSec + "   accele :" + mAcceleration);

		m_speedPerSec += mAcceleration * deltaSec;
		m_speedPerSec = Math.max(MinSpeedPerSec,
				Math.min(MaxSpeedPerSec, m_speedPerSec));
		
		m_speedPerSec = mThrust * MaxSpeedPerSec;
		
		mLocation.add(direction.mul(m_speedPerSec * deltaSec));

		pitchMatrix.trn(mLocation);
		m_combinedMatrix = pitchMatrix;

	}

	void Render() {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glPushMatrix();

		gl.glMultMatrixf(m_combinedMatrix.val, 0);

		mTexture.bind();
		mMesh.render(GL10.GL_TRIANGLES);
		gl.glPopMatrix();
	}


	public void SetPull(float pull) {
		pull = pull < 1 ? pull : 1;
		pull = pull > -1 ? pull : -1;
		m_pull = pull;
	}

	public void SetLean(float lean) {
		lean = lean < 1 ? lean : 1;
		lean = lean > -1 ? lean : -1;
		m_lean = lean;
	}

	public void setThrust(float thrust) {
		this.mThrust = thrust;
		float speedRatio = m_speedPerSec / MaxSpeedPerSec;
		mAcceleration = (thrust - (speedRatio))
				* (1 + Math.abs(speedRatio - 0.5f)) * 10;
	}

	public float getThrust() {
		return mThrust;
	}

	public Missile fireTo(Aircraft aircraft) {
		Matrix4 misMtx = m_combinedMatrix.cpy();

		Missile m = new Missile(misMtx);
		m.create();
		m.SetTarget(aircraft);

		return m;
	}

	@Override
	public Vector3 getLocation() {
		return mLocation;
	}

	@Override
	public Vector3 getDirection() {
		return mDirection;
	}

	public GameObject getLocked() {
		// TODO Auto-generated method stub
		return null;
	}

	public Matrix4 getCombinedMatrix() {
		return m_combinedMatrix;
	}

	public void setCombinedMatrix(Matrix4 m_combinedMatrix) {
		this.m_combinedMatrix = m_combinedMatrix;
	}
}
