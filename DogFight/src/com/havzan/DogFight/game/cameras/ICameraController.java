package com.havzan.dogfight.game.cameras;

import com.badlogic.gdx.graphics.Camera;

public interface ICameraController {

	public abstract void update();
	public abstract Camera getCamera();
	public abstract void setCamera(Camera camera);
	public abstract boolean setOnAir(boolean on);

}