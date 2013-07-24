package com.havzan.dogfight.game.cameras;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.havzan.dogfight.game.GameWorld;
import com.havzan.dogfight.game.model.GameModel;
import com.havzan.dogfight.game.model.IModelObject;

public class TrackCamera implements ICameraController {
	private IModelObject trackObj;
	public float trackDistance = 20;

	private Vector3 objPos = new Vector3();
	private Vector3 objDir = new Vector3();
	private Vector3 camPos = new Vector3();
	private Camera camera;
	private boolean isOn = false;
	
	public IModelObject getTrackObj() {
		return trackObj;
	}

	public void setTrackObj(IModelObject trackObj) {
		this.trackObj = trackObj;
	}
	
	/* (non-Javadoc)
	 * @see com.havzan.dogfight.game.cameras.ICameraController#update(com.badlogic.gdx.graphics.Camera)
	 */
	@Override
	public void update(){
		if (camera == null){
			throw new RuntimeException("Update call without any camera set");
		}
		if (trackObj == null){
			throw new RuntimeException("Track object is null");
		}
		
		trackObj.getLocation(objPos);
		trackObj.getDirection(objDir);
		
		calculateCamPos();
		
		camera.position.set(camPos);
		camera.direction.set(objDir);
		camera.up.set(GameModel.Up);
		
		camera.update();
	}
	
	private void calculateCamPos() {
		camPos.set(objPos.x - objDir.x * trackDistance,
				objPos.y - objDir.y * trackDistance,
				objPos.z - objDir.z * trackDistance);
	}

	@Override
	public boolean setOnAir(boolean on) {
		return this.isOn = (on && trackObj != null);
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
}
