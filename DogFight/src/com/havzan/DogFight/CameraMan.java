package com.havzan.dogfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraMan {
	private PerspectiveCamera mCamera;
	private IWorldObject mTrackedObjFrom = null;
	private IWorldObject mTrackedObjTo = null;
	private CameraMode mMode = CameraMode.NONE;

	public float trackOffsetDist = 10;
	public float trackOffsetLeft = -30;
	public float trackOffsetHeight = 2;

	public float mDistanceToTrack = 20;
	private float mFoV = 45;

	public enum CameraMode {
		NONE, TRACKMODE, FRONTOMODE
	}

	public CameraMan(PerspectiveCamera camera) {
		setCamera(camera);
	}

	public CameraMan(float width, float height) {
		mCamera = new PerspectiveCamera(mFoV, width, height);
		mCamera.up.set(0, 0, 1);
		mCamera.near = 5;
		mCamera.far = 300000;
		mCamera.direction.set(1, 0, 0);
		Gdx.input.setInputProcessor(null);
	}

	public void trackMode(IWorldObject objToTrack) {
		mTrackedObjFrom = objToTrack;
		mMode = CameraMode.TRACKMODE;
	}

	public void fromToMode(IWorldObject objFrom, IWorldObject objTo) {
		mTrackedObjFrom = objFrom;
		mTrackedObjTo = objTo;
		mMode = CameraMode.FRONTOMODE;
	}

	public PerspectiveCamera update(float deltaTime) {
		switch (getMode()) {
		case TRACKMODE: {
			if (mTrackedObjFrom == null)
				break;
			Vector3 trackPos = mTrackedObjFrom.getLocation();
			Vector3 trackDir = mTrackedObjFrom.getDirection().tmp().nor();

			getCamera().position.set(calculateCamPos(trackPos, trackDir).add(
					getCamera().up.tmp().scl(trackOffsetHeight)));

			getCamera().lookAt(trackPos.x, trackPos.y, trackPos.z);

			break;
		}
		case FRONTOMODE: {
			Vector3 trackFromPos = mTrackedObjFrom.getLocation();
			Vector3 fromToToDir = mTrackedObjTo.getLocation().tmp()
					.sub(mTrackedObjFrom.getLocation()).nor();

			mCamera.position.set(calculateCamPos(trackFromPos, fromToToDir));

			mCamera.direction.set(fromToToDir);

			break;
		}
		}

		mCamera.update();
		return mCamera;
	}

	private Vector3 calculateCamPos(Vector3 trackPos, Vector3 trackDir) {
		float distanceToCraft = mDistanceToTrack;

		return new Vector3(trackPos.x - trackDir.x * distanceToCraft,
				trackPos.y - trackDir.y * distanceToCraft, trackPos.z
						- trackDir.z * distanceToCraft);
	}

	public PerspectiveCamera getCamera() {
		return mCamera;
	}

	public void setCamera(PerspectiveCamera mCamera) {
		this.mCamera = mCamera;
	}

	public CameraMode getMode() {
		return mMode;
	}

	public void setMode(CameraMode mMode) {
		this.mMode = mMode;
	}
}
