package com.havzan.DogFight.game.ui;

import com.badlogic.gdx.math.Vector2;
import com.havzan.DogFight.game.model.Aircraft;

public class HUDTrackingData implements Comparable<HUDTrackingData> {
	public Vector2 mPosition = new Vector2();
	public boolean mIsLocked = false;
	public float mDistance2 = 0;
	public Aircraft aircraft;
	
	public HUDTrackingData(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	@Override
	public int compareTo(HUDTrackingData arg0) {
		if (mDistance2 == arg0.mDistance2)
			return 0;
		return mDistance2 < arg0.mDistance2 ? -1 : 1;
	}
}
