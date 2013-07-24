package com.havzan.DogFight;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Radar extends Group {
	private Image mBackImage;
	private IWorldObject mReference;
	Set<TrackableData> mTrackables = new HashSet<TrackableData>();

	Texture mTrackableTex;
	private final String TAG = "Radar";

	class TrackableData {
		public IWorldObject trackable;
		public boolean visible = false;
		public float x = 0;
		public float y = 0;
		public Image image = null;
		// public int type;
	}

	public Radar(String name) {
		super();

		Texture mBackTexture = new Texture(
				Gdx.files.internal("data/ui/radar.png"), true);
		mBackImage = new Image(mBackTexture);

		mBackImage.setTouchable(Touchable.disabled);
		addActor(mBackImage);

		setWidth(128);
		setHeight(128);

		mTrackableTex = new Texture(Gdx.files.internal("data/ui/red.png"), true);
		mTrackableTex.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
	}

//	@Override
//	public boolean touchDown(float x, float y, int pointer) {
//		return false;
//	}
//
//	@Override
//	public void touchDragged(float x, float y, int pointer) {
//
//		return;
//	}
//
//	@Override
//	public void touchUp(float x, float y, int pointer) {
//		// mSliding = false;
//		focus(null, pointer);
//		return;
//	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		update();
		
		float texWid = 4;
		float texHei = 4;

		float offsetX = this.getX() + this.getWidth() / 2 - texWid / 2;
		float offsetY = this.getY() + this.getHeight() / 2 - texHei / 2;

		for (TrackableData trackable : mTrackables) {

			if (trackable.visible) {
				batch.draw(mTrackableTex, trackable.x + offsetX, trackable.y
						+ offsetY, texWid, texHei);
			}
		}
	}

	public void setReference(IWorldObject reference) {
		mReference = reference;
	}

	public void addObjectToTrack(IWorldObject trackable) {
		TrackableData data = new TrackableData();
		data.trackable = trackable;
		data.image = new Image(mTrackableTex);

		mTrackables.add(data);
	}
	
	public void removeObjectToTrack(IWorldObject worldObj) {
		mTrackables.remove(worldObj);
	}

	public void update() {
		Vector2 refLoc = new Vector2(mReference.getLocation().x, mReference.getLocation().y);
		
		final Vector2 direction = new Vector2(mReference.getDirection().x, mReference.getDirection().y);
		float angleToY = (direction.angle() + 270) % 360;
		
		for (TrackableData trackable : mTrackables) {
			Vector2 trackableLoc = new Vector2(trackable.trackable.getLocation().x, trackable.trackable.getLocation().y);
			Vector2 toTrackableVector = trackableLoc.cpy().sub(refLoc);
			

			float toTrackableDistance = toTrackableVector.len();

			if (toTrackableDistance <= getMaxDistance()) {
				trackable.visible = true;
				
				toTrackableVector.rotate(-angleToY);

				Vector2 toTrackableNorm = toTrackableVector.nor();
				float toTrackableDistancePixels = toTrackableDistance
						/ getMaxDistance() * this.getWidth() / 2;
				
				//Gdx.app.log(TAG , "Y: " + toTrackableNorm.x + "    Z: " + toTrackableNorm.y);
				
				toTrackableNorm.mul(toTrackableDistancePixels);

				trackable.x = toTrackableNorm.x;
				trackable.y = toTrackableNorm.y;
				
			} else
				trackable.visible = false;
		}
	}

	private float getMaxDistance() {
		// TODO Auto-generated method stub
		return 5000;
	}
}
