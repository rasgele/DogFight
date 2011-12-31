package com.havzan.DogFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateBy;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;

public class WorldRenderer {
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
	
	interface IWorldPresenter {
		void onMissileFire();
		void onCameraSwitch();
		void onMarkerToggle();
	}

	
	WorldRenderer(IWorldPresenter presenter, float width, float height) {
		mWidth = width;
		mHeight = height;
		mPresenter = presenter;
	}

	public void create() {
		mCamMan = new CameraMan(mWidth, mHeight);
		createUI();
	}

	private void createUI() {
		mUI = new Stage(mWidth, mHeight, true);

		mSlider = new Slider("slider");

		mRadar = new Radar("radar");

		mCameraSwitchButton = new Button(ButtonStyleHelper.createDefaultButtonStyle(
				"data/ui/switchCamPressed.png", "data/ui/switchCam.png"));

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

		mMarkerToggleButton = new Button(ButtonStyleHelper.createDefaultButtonStyle(
				"data/ui/togMarkerPressed.png", "data/ui/togMarker.png"));
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

		mUI.addActor(rootTable);
	}

	public void render() {
		
	}
}
