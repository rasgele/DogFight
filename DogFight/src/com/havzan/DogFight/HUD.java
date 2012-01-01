package com.havzan.DogFight;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.havzan.DogFight.Radar.TrackableData;

public class HUD extends Group {

	public boolean mVisible;
	public boolean mLocked;

	public static class TrackData {
		public Vector2 mPosition = new Vector2();
		public boolean mIsLocked = false;
	}

	public ArrayList<TrackData> mPosition = new ArrayList<TrackData>();

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		Texture texLocked = Assets.getAsset("data/locked.png", Texture.class);
		Texture texTracked = Assets.getAsset("data/triangle.png", Texture.class);

		for (TrackData data : mPosition) {
			Texture tex = data.mIsLocked ? texLocked : texTracked;

			float texWid = tex.getWidth();
			float texHei = tex.getHeight();

			float offsetX = this.x + data.mPosition.x - texWid / 2;
			float offsetY = this.y + data.mPosition.y - texHei / 2;
			
			batch.draw(tex, offsetX, offsetY, texWid, texHei);
		}
	}
}
