package com.havzan.DogFight.game.cameras;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.havzan.DogFight.game.model.IModelObject;

public class ObjectToObjectCamera implements ICameraController {
	Camera camera;
	private IModelObject fromObj;
	private IModelObject toObj;
	private boolean isOn = false;
	
	private Vector3 fromObjPos = new Vector3();
	Vector3 fromToToDir = new Vector3();
	private Vector3 camPos = new Vector3();
	private float trackDistance = 20; 
	@Override
	public void update() {
		Vector3 trackFromPos = new Vector3();
		fromObj.getLocation(fromObjPos);
		
		Vector3 fromToToDir = new Vector3();
		toObj.getLocation(fromToToDir).sub(trackFromPos).nor();

		calculateCamPos();
		
		camera.position.set(camPos);

		camera.direction.set(fromToToDir);

		camera.update();
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	@Override
	public boolean setOnAir(boolean on) {
		return isOn  = (on && fromObj != null && toObj != null);
	}

	public IModelObject getFromObj() {
		return fromObj;
	}

	public void setFromObj(IModelObject fromObj) {
		this.fromObj = fromObj;
	}

	public IModelObject getToObj() {
		return toObj;
	}

	public void setToObj(IModelObject toObj) {
		this.toObj = toObj;
	}
	private void calculateCamPos() {
		camPos .set(fromObjPos.x - fromToToDir.x * trackDistance ,
				fromObjPos.y - fromToToDir.y * trackDistance,
				fromObjPos.z - fromToToDir.z * trackDistance);
	}
}
