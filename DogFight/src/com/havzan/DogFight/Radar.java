package com.test.myfirsttriangle;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public class Radar extends Group {
	private Image mBackImage;
	private IWorldObject mReference;
	ArrayList<TrackableData> mTrackables = new ArrayList<TrackableData>();

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
		super(name);

		// mButtonTexture = new
		// Texture(Gdx.files.internal("data/sliderHandle.png"), true);
		Texture mBackTexture = new Texture(
				Gdx.files.internal("data/ui/radar.png"), true);
		mBackImage = new Image("radar", mBackTexture);

		// mButton = new Button("_slider_button", mButtonTexture);
		// mButton.x = 0;
		// mButton.y = 0;

		mBackImage.touchable = false;
		addActor(mBackImage);
		// addActor(mButton);

		width = 128;
		height = 128;

		mTrackableTex = new Texture(Gdx.files.internal("data/red.png"), true);
		mTrackableTex.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
	}

	@Override
	protected boolean touchDown(float x, float y, int pointer) {
		return false;
	}

	@Override
	public void focus(Actor actor) {
		// TODO Auto-generated method stub
		focusedActor = actor;
		if (parent != null)
			parent.focus(actor == null ? null : this);

	}

	@Override
	protected boolean touchDragged(float x, float y, int pointer) {

		return false;
	}

	@Override
	protected boolean touchUp(float x, float y, int pointer) {
		// mSliding = false;
		focus(null);
		return false;
	}

	@Override
	protected void render(SpriteBatch batch) {
		super.render(batch);
		update();
		
		float texWid = 8;
		float texHei = 8;

		float offsetX = this.x + this.width / 2 - texWid / 2;
		float offsetY = this.y + this.height / 2 - texHei / 2;

		for (TrackableData trackable : mTrackables) {

			if (trackable.visible) {
				batch.draw(mTrackableTex, trackable.x + offsetX, trackable.y
						+ offsetY, 8, 8, 0, 0, texWid, texHei);
			}
		}
	}

	public void setReference(IWorldObject reference) {
		mReference = reference;
	}

	public void addObjectToTrack(IWorldObject trackable) {
		TrackableData data = new TrackableData();
		data.trackable = trackable;
		data.image = new Image("image", mTrackableTex);

		mTrackables.add(data);
	}

	public void update() {
		Vector3 refLoc = mReference.getLocation().cpy();
		
		final Vector2 direction = new Vector2(-mReference.getDirection().y, -mReference.getDirection().z);
		float angleToY = direction
		Matrix3 rotationMatrix = new Matrix3();
		rotationMatrix.setToRotation(angle)
		
		Gdx.app.log(TAG, "Direction X :" + direction.x + "  Direction Y : " + direction.y);

		for (TrackableData trackable : mTrackables) {
			Vector3 toTrackableVector = trackable.trackable.getLocation().cpy()
					.sub(refLoc);

			float toTrackableDistance = toTrackableVector.len();

			if (toTrackableDistance <= getMaxDistance()) {
				trackable.visible = true;

				Vector3 toTrackableNorm = toTrackableVector.cpy().nor();
				float toTrackableDistancePixels = toTrackableDistance
						/ getMaxDistance() * this.width / 2;
				
				Gdx.app.log(TAG , "Y: " + toTrackableNorm.y + "    Z: " + toTrackableNorm.z);
				
				toTrackableNorm.mul(toTrackableDistancePixels);

				trackable.x = -toTrackableNorm.y;
				trackable.y = -toTrackableNorm.z;
				
			} else
				trackable.visible = false;
		}
	}

	private float getMaxDistance() {
		// TODO Auto-generated method stub
		return 5000;
	}
}
