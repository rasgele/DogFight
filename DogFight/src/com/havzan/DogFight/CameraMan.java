package com.havzan.DogFight;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraMan {
	private PerspectiveCamera mCamera;
	private IWorldObject mTrackedObjFrom = null;
	private IWorldObject mTrackedObjTo = null;
	private int mCameraMode = NONE;

	public float trackOffsetX = 10;
	public float trackOffsetY = -30;
	public float trackOffsetZ = -30;

	final static int NONE = 0;
	final static int TRACKMODE = 1;
	final static int FROMTOMODE = 2;

	public CameraMan(PerspectiveCamera camera) {
		mCamera = camera;
	}

	public void trackMode(IWorldObject objToTrack) {
		mTrackedObjFrom = objToTrack;
	}

	public void fromToMode(IWorldObject objFrom, IWorldObject objTo) {
		mTrackedObjFrom = objFrom;
		mTrackedObjTo = objTo;
	}

	public void update(float deltaTime) {
		switch (mCameraMode) {
		case TRACKMODE: {
			
			Vector3 trackToPos = mTrackedObjFrom.getLocation();
			Vector3 trackToDir = mTrackedObjFrom.getDirection();
			
			float distanceToCraft = 50;
			mCamera.position.set(trackToPos.x + trackToDir.x * distanceToCraft,
					trackToPos.y + trackToDir.y * distanceToCraft,
					trackToPos.z + trackToDir.z * distanceToCraft);
			mCamera.direction.set(trackToDir);
		}
		}
	}
}
