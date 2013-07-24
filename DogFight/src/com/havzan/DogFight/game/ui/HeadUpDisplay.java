package com.havzan.DogFight.game.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.havzan.DogFight.MathUtil;
import com.havzan.DogFight.game.GameWorld;
import com.havzan.DogFight.game.model.Aircraft;
import com.havzan.DogFight.game.model.GameModel;

public class HeadUpDisplay extends Group {

	private static final String TAG = null;
	private final Drawable trackingDrawable;
	private final Image lockedImage;
	private ArrayList<Image> imagePool = new ArrayList<Image>();
	private final GameWorld world;
	private final Drawable lockedDrawable;

	public static class HeadUpDisplayStyle {
		public Drawable trackingDrawable;
		public Drawable lockedDrawable;
		public Drawable elevationDrawable;
	}

	private HashMap<Aircraft, HUDTrackingData> aircraft2Data = new HashMap<Aircraft, HUDTrackingData>();
	private HashMap<Aircraft, Image> aircraft2Image = new HashMap<Aircraft, Image>();
	private HashMap<Image, Aircraft> image2Aircraft = new HashMap<Image, Aircraft>();
	
	private float pitch = 0;
	private float roll = 0;
	private Image elevationImage;
	private float translateY;
	private Aircraft owner;

	public HeadUpDisplay(HeadUpDisplayStyle style, GameWorld world) {
		trackingDrawable = style.trackingDrawable;
		lockedDrawable = style.lockedDrawable;
		lockedImage = getImageFromPool();
		lockedImage.setDrawable(lockedDrawable);
		elevationImage = new Image(style.elevationDrawable);
		elevationImage.setOrigin(elevationImage.getWidth() / 2, elevationImage.getHeight() / 2);
		addActor(lockedImage);
		//addActor(elevationImage);
		//elevationImage.setOrigin(elevationImage.getImageWidth()/2, elevationImage.getImageHeight() / 2);
		this.world = world;

	}

	public HUDTrackingData getTrackingDataOf(Aircraft aircraft) {
		return aircraft2Data.get(aircraft);
	}

	public void addTrackingData(HUDTrackingData data) {
		assert aircraft2Data.containsKey(data.aircraft);
		aircraft2Data.put(data.aircraft, data);
		Image image = getImageFromPool();
		aircraft2Image.put(data.aircraft, image);
		image2Aircraft.put(image, data.aircraft);
		addActor(image);
	}

	public void removeTrackingData(Aircraft aircraft) {
		aircraft2Data.remove(aircraft);
		Image image = aircraft2Image.remove(aircraft);
		image2Aircraft.remove(image);
		returnImageToPool(image);
	}

	private Image getImageFromPool() {
		if (imagePool.size() > 0) {
			Image image = imagePool.get(imagePool.size() - 1);
			imagePool.remove(imagePool.size() - 1);
			return image;
		} else {
			Image image = new Image(trackingDrawable);
			image.addListener(new ClickListener() {
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Aircraft aircraft = image2Aircraft.get(event.getTarget());
					if (aircraft != null) {
						HUDTrackingData data = aircraft2Data.get(aircraft);
						world.setLockOnFoe(getOwner(), data.aircraft);						
					}
					return true;
				}
			});
			return image;
		}
	}

	private void returnImageToPool(Image img) {
		if (img != null) {
			imagePool.add(img);
			img.remove();
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		if (getOwner() == null  || !isVisible())
			return;
		
		calculateOrientation();
		
		lockedImage.setVisible(false);
		
		for ( Entry<Aircraft, HUDTrackingData> a2d  : this.aircraft2Data.entrySet()) {
			Aircraft aircraft = a2d.getKey();
			HUDTrackingData data = a2d.getValue();
			Image image = aircraft2Image.get(aircraft);

			if (data.mIsLocked) {
				image.setVisible(false);
				image = lockedImage;
			}
			image.setVisible(true);

			float texWid = image.getWidth();
			float texHei = image.getHeight();

			float offsetX = this.getX() + data.mPosition.x - texWid / 2;
			float offsetY = this.getY() + data.mPosition.y - texHei / 2;

			image.setX(offsetX);
			image.setY(offsetY);

		}
		elevationImage.setScale(getHeight() / elevationImage.getImageHeight() * 2);
		elevationImage.setX(getWidth() / 2 - elevationImage.getScaleX() * elevationImage.getWidth() / 2);
		elevationImage.setY(0);
		elevationImage.translate(0, translateY);
		elevationImage.setRotation(roll);
		elevationImage.toBack();

		// for (HUDTrackingData data : mPosition) {
		// Drawable tex = data.mIsLocked ? lockedDrawable : trackingDrawable;
		//
		// float texWid = tex.getMinWidth();
		// float texHei = tex.getMinHeight();
		//
		// float offsetX = this.getX() + data.mPosition.x - texWid / 2;
		// float offsetY = this.getY() + data.mPosition.y - texHei / 2;
		//
		// tex.draw(batch, offsetX, offsetY, texWid, texHei);
		// // batch.draw(tex, offsetX, offsetY, texWid, texHei);
		// }
	}

	private void calculateOrientation() {
		Aircraft player = getOwner();
		Vector3 dir = player.getDirection(new Vector3());
		dir.z = 0;
		final Vector3 initDir = player.getInitDirection(new Vector3());
		float angle = (float) MathUtil.getAngleBetween(initDir, dir);
		translateY = (float) (angle / Math.PI / 2 * getHeight());
		
		Vector3 right = player.getRight(new Vector3());
		final Vector3 initRight = player.getInitRight(new Vector3());
		right.x = 0;
		roll = (float) Math.toDegrees(MathUtil.getAngleBetween(initRight, right));
		right.crs(initRight);
		roll *= Math.signum(right.x);
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public Aircraft getOwner() {
		return owner;
	}
	
	public Aircraft setOwner(Aircraft owner) {
		return this.owner = owner;
	}
}
