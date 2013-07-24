package com.havzan.DogFight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.havzan.DogFight.HUD;
import com.havzan.DogFight.game.model.Aircraft;

public class PlayerController {
	private int mInitialRoll;
	private int mInitialPitch;

	private Aircraft player;

	public void updateControls() {
		updatePlayerCommands();
		
//		if (Gdx.input.justTouched()) {
//			final float hitThreshold2 = 10f * 10f;
//			final Vector2 touch = new Vector2(Gdx.input.getX(),
//					Gdx.graphics.getHeight() - Gdx.input.getY());
//
//			for (HUD.TrackData trackData : mHUD.mPosition) {
//				Gdx.app.log(TAG, "Touch : " + touch + "\tPos : "
//						+ trackData.mPosition);
//				if (hitThreshold2 > trackData.mPosition.dst2(touch)) {
//					trackData.mIsLocked = true;
//					mWorld.setLocked(player, trackData.mAirCraft);
//					break;
//				}
//			}
//		}
	}

	private void updatePlayerCommands() {
		float azimuth = Gdx.input.getAzimuth();
		float pitch = Gdx.input.getPitch();
		float roll = Gdx.input.getRoll();

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			roll = -90 + mInitialRoll;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			roll = 90 + mInitialRoll;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			pitch = 90 + mInitialPitch;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			pitch = -90 + mInitialPitch;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.MENU)) {
			// resetInitialOrientation();
		}

		Aircraft player = getPlayer();
		if (player != null) {
			player.setStickY((pitch - mInitialRoll) / -45);
			player.setStickX((roll - mInitialPitch) / 30);
		}
	}

	public Aircraft getPlayer() {
		return player;
	}

	public void setPlayer(Aircraft player) {
		this.player = player;
	}
}
