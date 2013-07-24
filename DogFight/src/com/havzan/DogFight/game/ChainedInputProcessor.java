package com.havzan.DogFight.game;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;

public class ChainedInputProcessor implements InputProcessor{
	public ArrayList<InputProcessor> processors = new ArrayList<InputProcessor>();
	@Override
	public boolean keyDown(int keycode) {
		boolean handled = false;
		for(InputProcessor p : processors)
			if (handled = p.keyDown(keycode))
				break;
		return handled;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean handled = false;
		for(InputProcessor p : processors)
			if (handled = p.keyUp(keycode))
				break;
		return handled;
	}

	@Override
	public boolean keyTyped(char character) {
		boolean handled = false;
		for(InputProcessor p : processors)
			if (handled = p.keyTyped(character))
				break;
		return handled;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean handled = false;
		for(InputProcessor p : processors)
			if (handled = p.touchDown(screenX, screenY, pointer, button))
				break;
		return handled;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean handled = false;
		for(InputProcessor p : processors)
			if (handled = p.touchUp(screenX, screenY, pointer, button))
				break;
		return handled;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		boolean handled = false;
		for(InputProcessor p : processors)
			if (handled = p.touchDragged(screenX, screenY, pointer))
				break;
		return handled;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		boolean handled = false;
		for(InputProcessor p : processors)
			if (handled = p.mouseMoved(screenX, screenY))
				break;
		return handled;
	}

	@Override
	public boolean scrolled(int amount) {
		boolean handled = false;
		for(InputProcessor p : processors)
			if (handled = p.scrolled(amount))
				break;
		return handled;
	}

}
