package com.havzan.DogFight;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

public class HUD extends Group {

	public boolean mVisible;
	public boolean mLocked;

	public static class TrackData implements Comparable<TrackData> {
		public Vector2 mPosition = new Vector2();
		public boolean mIsLocked = false;
		public float mDistance2 = 0;
		public Aircraft mAirCraft;

		@Override
		public int compareTo(TrackData arg0) {
			if (mDistance2 == arg0.mDistance2)
				return 0;
			return mDistance2 < arg0.mDistance2 ? -1 : 1;
		}
	}

	public List<TrackData> mPosition = new LinkedList<TrackData>();

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		Texture texLocked = Assets.getAsset("data/locked.png", Texture.class);
		Texture texTracked = Assets
				.getAsset("data/triangle.png", Texture.class);

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
