package com.havzan.dogfight.game.cameras;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.havzan.dogfight.game.model.IModelObject;

public class CameraDirector {
	ArrayList<ICameraController> controllers = new ArrayList<ICameraController>();
	private PerspectiveCamera camera;
	private TrackCamera trackCam;
	private FreeCamera freeCam;
	private int currentCamIndex;
	private ObjectToObjectCamera obj2objCam;
	
	public CameraDirector() {
		create();
	}
	
	void create(){
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// camera.up.set(0, 0, 1);
		camera.near = 0.1f;
		camera.far = 300000;
		// camera.direction.set(1, 0, 0);
		camera.position.set(0, 0, 50);
		camera.lookAt(0, 0, 0);
		
		trackCam = new TrackCamera();
		trackCam.setCamera(camera);
		
		obj2objCam = new ObjectToObjectCamera();
		
		freeCam = new FreeCamera(camera);
		
		controllers.add(trackCam);
		controllers.add(obj2objCam);
		controllers.add(freeCam);
		
		freeCam.setOnAir(true);
		currentCamIndex = controllers.indexOf(freeCam);
	}
	
	public void setTrackObj(IModelObject obj){
		trackCam.setTrackObj(obj);
		obj2objCam.setFromObj(obj);
	}
	
	public void setTrackToObj(IModelObject obj){
		obj2objCam.setToObj(obj);
	}
	
	public void switchNextCam(){
		controllers.get(currentCamIndex).setOnAir(false);
		boolean on = false;
		do
		{
			currentCamIndex = (currentCamIndex + 1 ) % controllers.size();
			on = controllers.get(currentCamIndex).setOnAir(true);
		} while (!on );
	}

	public Camera getCamera() {
		return camera;
	}

	public InputProcessor getUIController() {
		return freeCam;
	}

	public void update(float deltaSecs) {
		controllers.get(currentCamIndex).update();
	}
}
