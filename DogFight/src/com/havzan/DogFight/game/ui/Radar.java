package com.havzan.DogFight.game.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.havzan.DogFight.MathUtil;
import com.havzan.DogFight.game.model.Aircraft;
import com.havzan.DogFight.game.model.GameModel;
import com.havzan.DogFight.game.model.IModelObject;
import com.havzan.DogFight.game.model.Missile;

public class Radar extends Group {
	private Image mBackImage;
	private IModelObject mReference;
	Set<TrackableData> mTrackables = new HashSet<TrackableData>();

	private final String TAG = "Radar";
	private Drawable background;
	private Drawable aircraftDrawable;
	private Drawable missileDrawable;

	static public class RadarStyle {
		public RadarStyle() {
		}

		public Drawable background;
		public Drawable aircraft;
		public Drawable missile;
	}

	class TrackableData {
		public IModelObject trackable;
		public boolean visible = false;
		public float x = 0;
		public float y = 0;
		// public Image image = null;
		// public int type;
		public float directionDifference;
	}

	public Radar(RadarStyle style) {
		super();

		setStyle(style);
		mBackImage = new Image(background);

		mBackImage.setTouchable(Touchable.disabled);
		addActor(mBackImage);

		setWidth(128);
		setHeight(128);
	}

	public void init(GameModel model) {
		for (Aircraft a : model.getAircrafts())
			if (mReference != a)
				addObjectToTrack(a);
		for (Missile m : model.getMissiles())
			addObjectToTrack(m);
	}

	private void setStyle(RadarStyle style) {
		this.background = style.background;
		this.aircraftDrawable = style.aircraft;
		this.missileDrawable = style.missile;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		update();
		
		for (TrackableData data : mTrackables) {
			float texWid;
			float texHei;
			TextureRegionDrawable drawable;
			if (data.trackable instanceof Aircraft){
				texWid = texHei = 12;
				drawable = (TextureRegionDrawable) aircraftDrawable;
			}
			else{
				texWid = 4;
				texHei = 6;
				drawable = (TextureRegionDrawable) missileDrawable;
			}
			
			float offsetX = this.getX() + this.getWidth() / 2 - texWid / 2;
			float offsetY = this.getY() + this.getHeight() / 2 - texHei / 2;
			
			if (data.visible) {
//				drawable.draw(batch, trackable.x + offsetX, trackable.y
//						+ offsetY, texWid, texHei);
				batch.draw(drawable.getRegion(), data.x + offsetX, data.y
						+ offsetY, texWid / 2, texHei / 2, 
						texWid, texHei, 1, 1, data.directionDifference);
			}
		}
	}

	public void setReference(IModelObject reference) {
		mReference = reference;
	}

	public void addObjectToTrack(IModelObject trackable) {
		TrackableData data = new TrackableData();
		data.trackable = trackable;
		// data.image = new Image(mTrackableTex);

		mTrackables.add(data);
	}

	public void removeObjectToTrack(IModelObject worldObj) {
		for (Iterator<TrackableData> it = mTrackables.iterator(); it.hasNext();) {
			TrackableData data = it.next();
			if (data.trackable == worldObj) {
				mTrackables.remove(data);
				break;
			}
		}
	}

	public void update() {
		if (mReference == null)
			return;
		Vector3 refloc3 = mReference.getLocation(new Vector3());
		Vector2 refLoc = new Vector2(refloc3.z, refloc3.x);

		Vector3 refDirection3 = mReference.getDirection(new Vector3());

		final Vector2 direction = new Vector2(refDirection3.z, refDirection3.x);
		float angleToY = 90 - direction.angle();

		for (TrackableData trackable : mTrackables) {
			Vector3 trackableLoc3 = trackable.trackable
					.getLocation(new Vector3());
			Vector2 trackableLoc = new Vector2(trackableLoc3.z, trackableLoc3.x);
			Vector2 toTrackableVector = trackableLoc.cpy().sub(refLoc);
			

			float toTrackableDistance = toTrackableVector.len();

			if (toTrackableDistance <= getMaxDistance()) {
				trackable.visible = true;

				toTrackableVector.rotate(angleToY);

				Vector2 toTrackableNorm = toTrackableVector.nor();
				float toTrackableDistancePixels = (toTrackableDistance / getMaxDistance())
						* this.getWidth() / 2;

				// Gdx.app.log(TAG , "Y: " + toTrackableNorm.x + "    Z: " +
				// toTrackableNorm.y);

				toTrackableNorm.scl(toTrackableDistancePixels);

				trackable.x = toTrackableNorm.x;
				trackable.y = toTrackableNorm.y;				

				Vector3 trackableDir3 = trackable.trackable.getDirection(new Vector3());
				Vector2 trackableDir = new Vector2(trackableDir3.z, trackableDir3.x);
				
				trackable.directionDifference = trackableDir.angle() - direction.angle();
				

			} else
				trackable.visible = false;
		}
	}

	private float getMaxDistance() {
		return 5000;
	}
}
