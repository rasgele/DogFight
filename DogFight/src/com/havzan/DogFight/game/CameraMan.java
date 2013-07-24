package com.havzan.dogfight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.havzan.dogfight.game.model.IModelObject;

public class CameraMan {
	private PerspectiveCamera camera;
	private IModelObject mTrackedObjFrom = null;
	private IModelObject mTrackedObjTo = null;
	private CameraMode currentMode = CameraMode.FREE;

	public float trackOffsetDist = 10;
	public float trackOffsetLeft = -30;
	public float trackOffsetHeight = 2;

	public float mDistanceToTrack = 20;
	private float fieldOfView = 67;
	CameraInputController camController;

	public enum CameraMode {
		FREE, TRACKMODE, OBJECT2OBJECTMODE
	}

	public CameraMan(PerspectiveCamera camera) {
		setCamera(camera);
	}

	public CameraMan(float width, float height) {

		camera = new PerspectiveCamera(fieldOfView, width, height);
		// camera.up.set(0, 0, 1);
		camera.near = 0.1f;
		camera.far = 300000;
		// camera.direction.set(1, 0, 0);
		camera.position.set(0, 0, 50);
		camera.lookAt(0, 0, 0);

		createController();

		setMode(CameraMode.FREE);
	}

	public void trackMode(IModelObject objToTrack) {
		mTrackedObjFrom = objToTrack;
		setMode(CameraMode.TRACKMODE);
	}

	public void fromToMode(IModelObject objFrom, IModelObject objTo) {
		mTrackedObjFrom = objFrom;
		mTrackedObjTo = objTo;
		setMode(CameraMode.OBJECT2OBJECTMODE);
	}

	public CameraMode switchMode() {
		CameraMode[] modes = CameraMode.values();
		int modeIndex = -1;
		for (int i = 0; i < modes.length; i++) {
			if (getMode() == modes[i]) {
				modeIndex = (i + 1) % modes.length;
				break;
			}
		}

		if (modeIndex == -1)
			throw new RuntimeException("Selected mode cannot be found");

		boolean modeAssigned = false;
		while (!modeAssigned) {
			switch (modes[modeIndex]) {
			case OBJECT2OBJECTMODE:
				if (mTrackedObjFrom == null || mTrackedObjTo == null)
					modeIndex = (modeIndex + 1) % modes.length;
				else
					modeAssigned = true;
				break;
			case TRACKMODE:
				if (mTrackedObjFrom == null)
					modeIndex = (modeIndex + 1) % modes.length;
				else
					modeAssigned = true;
				break;
			case FREE:
				modeAssigned = true;
				createController();
				break;
			}
		}
		if (getMode() != modes[modeIndex])
		{
			setMode(modes[modeIndex]);
		}
		return modes[modeIndex];
	}

	private void createController() {
		Camera cam = new PerspectiveCamera(fieldOfView, camera.viewportWidth,
				camera.viewportHeight);
		// camera.up.set(0, 0, 1);
		cam.near = 0.1f;
		cam.far = 300000;
		// camera.direction.set(1, 0, 0);

		cam.position.set(camera.position);
		cam.direction.set(camera.direction);
		cam.normalizeUp();
		if (camController == null)
			camController = new CameraInputController(cam);
		else
			camController.camera = cam;
		if (mTrackedObjFrom != null)
			mTrackedObjFrom.getLocation(camController.target);
		camController.update();
		cam.update();
	}

	public PerspectiveCamera update(float deltaTime) {
		switch (getMode()) {
		case TRACKMODE: {
			if (mTrackedObjFrom == null)
				break;
			Vector3 trackPos = new Vector3();
			mTrackedObjFrom.getLocation(trackPos);
			Vector3 trackDir = new Vector3();
			mTrackedObjFrom.getDirection(trackDir).nor();

			getCamera().position.set(calculateCamPos(trackPos, trackDir).add(
					getCamera().up.tmp().scl(trackOffsetHeight)));

			getCamera().lookAt(trackPos.x, trackPos.y, trackPos.z);

			camera.update();
			break;
		}
		case OBJECT2OBJECTMODE: {
			Vector3 trackFromPos = new Vector3();
			mTrackedObjFrom.getLocation(trackFromPos);
			Vector3 fromToToDir = new Vector3();
			mTrackedObjTo.getLocation(fromToToDir).sub(trackFromPos).nor();

			camera.position.set(calculateCamPos(trackFromPos, fromToToDir));

			camera.direction.set(fromToToDir);

			camera.update();
			break;
		}
		case FREE: {
			camController.update();
		}
		default:
			assert false;
			break;
		}

		return camera;
	}

	private Vector3 calculateCamPos(Vector3 trackPos, Vector3 trackDir) {
		float distanceToCraft = mDistanceToTrack;

		return new Vector3(trackPos.x - trackDir.x * distanceToCraft,
				trackPos.y - trackDir.y * distanceToCraft, trackPos.z
						- trackDir.z * distanceToCraft);
	}

	public PerspectiveCamera getCamera() {
		return (PerspectiveCamera) (getMode() == CameraMode.FREE ? camController.camera
				: camera);
	}

	public void setCamera(PerspectiveCamera mCamera) {
		this.camera = mCamera;
	}

	public CameraMode getMode() {
		return currentMode;
	}

	public void setMode(CameraMode mode) {
		this.currentMode = mode;
		if (mode == CameraMode.FREE) {
			// Gdx.input.setInputProcessor(camController);
		} else if (Gdx.input.getInputProcessor() == camController) {
			// Gdx.input.setInputProcessor(null);
		}
	}

	public void setTrackFrom(IModelObject obj) {
		// TODO Auto-generated method stub
		mTrackedObjFrom = obj;
	}

	public void setTrackTo(IModelObject obj) {
		mTrackedObjTo = obj;

	}
}
