package com.havzan.DogFight.game.cameras;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class FreeCamera implements ICameraController, InputProcessor {
	private boolean isOn = false;
	private CameraInputController controller;
	private Camera camera;

	public FreeCamera(Camera camera) {
		createController(camera);
	}

	private void createController(Camera camera) {
		controller = new CameraInputController(camera);
		controller.autoUpdate = true;
	}

	@Override
	public boolean keyDown(int keycode) {
		return isOn ? this.controller.keyDown(keycode) : false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return isOn ? this.controller.keyUp(keycode) : false;
	}

	@Override
	public boolean keyTyped(char character) {
		return isOn ? this.controller.keyTyped(character) : false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return isOn ? this.controller.touchDown(screenX, screenY, pointer,
				button) : false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return isOn ? this.controller
				.touchUp(screenX, screenY, pointer, button) : false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return isOn ? this.controller.touchDragged(screenX, screenY, pointer)
				: false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return isOn ? this.controller.mouseMoved(screenX, screenY) : false;
	}

	@Override
	public boolean scrolled(int amount) {
		return isOn ? this.controller.scrolled(amount) : false;
	}

	@Override
	public boolean setOnAir(boolean on) {
		this.isOn = on;
		return true;
	}

	@Override
	public void update() {
		controller.update();
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public void setCamera(Camera camera) {
		this.camera = camera;
		createController(this.camera);
	}
}
