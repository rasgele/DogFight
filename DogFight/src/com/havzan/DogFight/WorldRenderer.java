package com.havzan.DogFight;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateBy;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.havzan.DogFight.HUD.TrackData;
import com.havzan.DogFight.World.IWorldEventListener;

public class WorldRenderer {
	private static final String TAG = "RENDERER";
	private IWorldPresenter mPresenter;
	private Stage mUI;
	private Slider mSlider;
	private Radar mRadar;
	private Button mCameraSwitchButton;
	private CameraMan mCamMan;

	private float mWidth;
	private float mHeight;
	private Button mFireButton;
	private Button mMarkerToggleButton;
	protected boolean mShowMarker = false;
	private World mWorld;

	float[] lightColor = { 1, 1, 1, 0 };
	float[] lightPosition = { 2, 5, 10, 0 };
	private float mInitialRoll;
	private float mInitialPitch;
	private Vector3 mTouchPoint = new Vector3();

	private HUD mHUD;

	interface IWorldPresenter {
		void onMissileFire();

		void onCameraSwitch();

		void onMarkerToggle();
	}

	public WorldRenderer(IWorldPresenter presenter, World world, float width,
			float height) {
		mWidth = width;
		mHeight = height;
		mPresenter = presenter;
		mWorld = world;

		mWorld.setWorldEventListener(new IWorldEventListener() {

			@Override
			public void aircraftAdded(Aircraft aircraft) {
				mRadar.addObjectToTrack(aircraft);
			}

			@Override
			public void missileAdded(Missile missile) {
				mRadar.addObjectToTrack(missile);
			}

			@Override
			public void aircraftRemoved(Aircraft aircraft) {
				mRadar.removeObjectToTrack(aircraft);

			}

			@Override
			public void missileRemoved(Missile missile) {
				mRadar.removeObjectToTrack(missile);
			}
		});
	}

	public WorldRenderer create() {
		mCamMan = new CameraMan(mWidth, mHeight);
		mCamMan.trackMode(mWorld.getPlayer());
		createUI();
		Gdx.input.setInputProcessor(mUI);

		for (Aircraft a : mWorld.getAircrafts()) {
			mRadar.addObjectToTrack(a);
		}

		for (Missile m : mWorld.getMissiles()) {
			mRadar.addObjectToTrack(m);
		}
		return this;
	}

	private void createUI() {
		mUI = new Stage(mWidth, mHeight, true);

		mSlider = new Slider("slider");

		mRadar = new Radar("radar");
		mRadar.setReference(mWorld.getPlayer());

		mCameraSwitchButton = new Button(
				ButtonStyleHelper
						.createDefaultButtonStyle(
								"data/ui/switchCamPressed.png",
								"data/ui/switchCam.png"));

		mCameraSwitchButton.width = mCameraSwitchButton.getPrefWidth();
		mCameraSwitchButton.height = mCameraSwitchButton.getPrefHeight();

		mCameraSwitchButton.setClickListener(new ClickListener() {
			@Override
			public void click(Actor actor) {
				mPresenter.onCameraSwitch();
				// actor.action(RotateBy.$(360, 0.4f));
				//
				// if (mCamMan.getMode() == CameraMode.TRACKMODE) {
				// if (missile != null) {
				// mCamMan.fromToMode(missile, droneCraft);
				// mCamMan.mDistanceToTrack = 10;
				// } else {
				// mCamMan.fromToMode(aircraft, droneCraft);
				// mCamMan.mDistanceToTrack = 50;
				// }
				// } else {
				// mCamMan.trackMode(aircraft);
				// mCamMan.mDistanceToTrack = 50;
				// }
			}
		});

		mFireButton = new Button(ButtonStyleHelper.createDefaultButtonStyle(
				"data/ui/firePressed.png", "data/ui/fire.png"));

		mFireButton.setClickListener(new ClickListener() {
			@Override
			public void click(Actor actor) {
				actor.action(RotateBy.$(360, 0.4f));
				mPresenter.onMissileFire();
				// if (missile != null)
				// MarkerManager.getInstance().unregisterMarker(missile);
				// missile = aircraft.fireTo(droneCraft);
				// MarkerManager.getInstance().registerMarker(missile);
				//
				// if (missile != null) {
				// mCamMan.fromToMode(missile, droneCraft);
				// mCamMan.mDistanceToTrack = 10;
				// }
			}
		});

		mMarkerToggleButton = new Button(
				ButtonStyleHelper
						.createDefaultButtonStyle(
								"data/ui/togMarkerPressed.png",
								"data/ui/togMarker.png"));
		mMarkerToggleButton.setClickListener(new ClickListener() {
			@Override
			public void click(Actor actor) {
				actor.action(RotateBy.$(360, 0.4f));
				mPresenter.onMarkerToggle();
			}
		});

		mCameraSwitchButton.size(32, 32);
		mMarkerToggleButton.size(32, 32);
		mFireButton.size(32, 32);

		Table group = new Table();

		group.add(mCameraSwitchButton).pad(2, 1, 2, 1);
		group.add(mMarkerToggleButton).pad(2, 1, 2, 1);
		group.add(mFireButton).pad(2, 1, 2, 1);

		Table rootTable = new Table();
		rootTable.debug();
		rootTable.width = Gdx.graphics.getWidth();
		rootTable.height = Gdx.graphics.getHeight();

		rootTable.size(100, 100);

		rootTable.add(group).colspan(2).right().top().expandY();

		rootTable.row().expandX().expandY();
		rootTable.add(mRadar).left().bottom().pad(0, 2, 2, 0);
		rootTable.add(mSlider).right().bottom().pad(0, 0, 2, 2);

		rootTable.layout();

		rootTable.debug("all");

		mHUD = new HUD();

		mHUD.addActor(rootTable);

		mUI.addActor(mHUD);
	}

	public void render(float deltaTime) {
		GL10 gl = Gdx.graphics.getGL11();

		gl.glClearColor(.2f, .4f, 1, 1);
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightColor, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0);

		setupCamera(deltaTime, gl);
		renderTerrain(gl);
		renderAircrafts(gl);
		renderMissiles(gl);
		renderHUD();
		renderUI(deltaTime);
	}

	private void renderTerrain(GL10 gl) {
		gl.glPushMatrix();
		gl.glScalef(10000, 10000, 10000);

		Assets.getTerrainTexture().bind();

		Assets.getTerrainModel().render(GL10.GL_TRIANGLES);

		gl.glPopMatrix();

	}

	private void renderUI(float deltaTime) {
		mUI.act(deltaTime);
		mUI.draw();
	}

	private void renderHUD() {
		// TODO Auto-generated method stub
		prepareHUDTrackingData();
	}

	private void prepareHUDTrackingData() {
		Aircraft player = mWorld.getPlayer();

		Collection<Aircraft> trackables = mWorld.getTrackables(player);
		BoundingBox box = new BoundingBox();
		Assets.getAircraftModel().calculateBoundingBox(box);

		PerspectiveCamera cam = mCamMan.getCamera();

		Aircraft locked = player.getLocked();

		mHUD.mPosition.clear();

		Vector3 playerLoc = player.getLocation();

		for (Aircraft trackable : trackables) {
			Vector3 proj = trackable.getLocation().cpy();
			cam.project(proj);

			if (proj.z < 1.0f) {
				HUD.TrackData data = new HUD.TrackData();
				data.mPosition.set(proj.x, proj.y);
				data.mIsLocked = (trackable == locked);
				data.mDistance2 = trackable.getLocation().tmp2().sub(playerLoc)
						.len2();
				data.mAirCraft = trackable;
				mHUD.mPosition.add(data);
			}
		}

		Collections.sort(mHUD.mPosition, new Comparator<HUD.TrackData>() {
			@Override
			public int compare(TrackData arg0, TrackData arg1) {
				return arg0.compareTo(arg1);
			}
		});
	}

	private void renderMissiles(GL10 gl) {
		for (Missile missile : mWorld.getMissiles()) {
			renderMissile(gl, missile);
		}

	}

	private void renderMissile(GL10 gl, Missile missile) {
		renderModel(gl, Assets.getMissileModel(), Assets.getMissileTexture(),
				missile.getCombinedMatrix());
	}

	private void renderAircrafts(GL10 gl) {
		Aircraft player = mWorld.getPlayer();
		renderAircraft(gl, player);

		for (Aircraft aircraft : mWorld.getAircrafts()) {
			renderAircraft(gl, aircraft);
		}
	}

	private void renderAircraft(GL10 gl, Aircraft aircraft) {
		renderModel(gl, Assets.getAircraftModel(), Assets.getAircraftTexture(),
				aircraft.getCombinedMatrix());
	}

	private void renderModel(GL10 gl, Mesh model, Texture texture,
			Matrix4 matrix) {
		if (matrix != null) {
			gl.glPushMatrix();

			gl.glMultMatrixf(matrix.val, 0);
		}

		if (texture != null)
			texture.bind();

		model.render(GL10.GL_TRIANGLES);
		if (matrix != null)
			gl.glPopMatrix();
	}

	private void setupCamera(float deltaTime, GL10 gl) {
		mCamMan.update(deltaTime).apply(gl);
	}

	public void updateControls() {
		float azimuth = Gdx.input.getAzimuth();
		float pitch = Gdx.input.getPitch();
		float roll = Gdx.input.getRoll();

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
			pitch = 90 + mInitialRoll;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
			pitch = -90 + mInitialRoll;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
			roll = 90 + mInitialPitch;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
			roll = -90 + mInitialPitch;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.MENU)) {
			resetInitialOrientation();
		}

		Aircraft player = mWorld.getPlayer();
		player.SetLean((pitch - mInitialRoll) / -45);
		player.SetPull((roll - mInitialPitch) / 30);

		player.setThrust(mSlider.getPosition());

		prepareHUDTrackingData();

		if (Gdx.input.justTouched()) {
			final float hitThreshold2 = 10f * 10f;
			final Vector2 touch = new Vector2(Gdx.input.getX(),
					Gdx.graphics.getHeight() - Gdx.input.getY());

			for (HUD.TrackData trackData : mHUD.mPosition) {
				Gdx.app.log(TAG, "Touch : " + touch + "\tPos : "
						+ trackData.mPosition);
				if (hitThreshold2 > trackData.mPosition.dst2(touch)) {
					trackData.mIsLocked = true;
					mWorld.setLocked(player, trackData.mAirCraft);
					break;
				}
			}
		}
		// if (Gdx.input.justTouched()) {
		//
		// mCamMan.getCamera().unproject(
		// mTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		// Ray ray = mCamMan.getCamera().getPickRay(Gdx.input.getX(),
		// Gdx.input.getY());
		//
		// Collection<Aircraft> trackables = mWorld.getTrackables(player);
		// BoundingBox box = new BoundingBox();
		// Assets.getAircraftModel().calculateBoundingBox(box);
		//
		// for (Aircraft trackable : trackables) {
		// box.mul(trackable.getCombinedMatrix());
		// if (Intersector.intersectRayBoundsFast(ray, box)) {
		// Gdx.app.log(TAG, "Hit test BB!");
		// mWorld.setLocked(player, trackable);
		// }
		// }
		// }
	}

	private void resetInitialOrientation() {
		mInitialRoll = Gdx.input.getPitch();
		mInitialPitch = Gdx.input.getRoll();
	}
}
