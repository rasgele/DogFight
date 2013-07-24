package com.havzan.DogFight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.havzan.DogFight.game.GameLoopScreen;
import com.havzan.DogFight.game.model.Aircraft;
import com.havzan.DogFight.game.model.AircraftParams;
import com.havzan.DogFight.game.model.GameModel;
import com.havzan.DogFight.game.model.Missile;
import com.havzan.DogFight.game.model.MissileParams;
import com.havzan.DogFight.game.model.Terrain;

public class DogFightGame extends Game{
	MainGameScreen mMainGameScreen;
	
	@Override
	public void create() {
		Assets.load();
		//mMainGameScreen = new MainGameScreen(this);
		GameLoopScreen mGameLoopScreen = new GameLoopScreen(this, getModel());
		setScreen(mGameLoopScreen);
	}
	
	GameModel getModel(){
		GameModel model = new GameModel();
		
		Vector3 initDirection = new Vector3(1, 0, 0);
		model.addPlayer(new Aircraft("Player", "f22", new AircraftParams(), initDirection)).setThrust(0.0f).setOrientation(new Matrix4().translate(10, 0, 0));
		
		Aircraft a2 = new Aircraft("Drone 1", "f22", new AircraftParams(), initDirection);
		a2.setOrientation(new Matrix4().rotate(1, 0, 0, 30).translate(2000, 0, 0));
		model.addAircraft(a2);
		Aircraft a3 = new Aircraft("Drone 2", "f22", new AircraftParams(), initDirection);
		a3.setOrientation(new Matrix4().translate(200, -100, 0));
		model.addAircraft(a3);
		Aircraft a4 = new Aircraft("Drone 3", "f22", new AircraftParams(), initDirection);
		a4.setOrientation(new Matrix4().translate(0, 100,- 50));
		model.addAircraft(a4);
		
		model.terrain = new Terrain(600, 600);
		
		Missile m = new Missile("Missile 1", new MissileParams(), model.getPlayer(), a3);
		m.setOrientation(new Matrix4().translate(0, 0, 0));
		model.addMissile(m);
		
		
		return model;
	}
//	CameraMan mCamMan;
//	Mesh mesh;
//	Mesh grass;
//	Texture texture;
//	Texture grassTexture;
//	float angleY = 0;
//	float angleX = 0;
//	float[] lightColor = { 1, 1, 1, 0 };
//	float[] lightPosition = { 2, 5, 10, 0 };
//	float touchStartX = 0;
//	float touchStartY = 0;
//	Aircraft aircraft;
//	Missile missile;
//
//	Aircraft droneCraft;
//
//	BitmapFont font;
//	SpriteBatch batch;
//	PathMarker marker;
//
//	float initialRoll;
//	float initialPitch;
//
//	int cameraMode = 0;
//	private SkyBox m_skybox;
//	static final int MAX_CAM_MODE = 5;
//
//	Stage ui;
//	Slider slider;
//	Button cameraSwitchButton;
//	Button markerToggleButton;
//
//	boolean mShowMarker = false;
//	private Button fireButton;
//	private Radar radar;
//
//	AssetManager mAssetManager;
//
//	@Override
//	public void create() {
//		initializeAssetManager();
//
//		createUI(mAssetManager);
//
//		aircraft = new Aircraft();
//		aircraft.create(mAssetManager);
//		aircraft.getLocation().set(0, 0, 2000);
//
//		droneCraft = new Aircraft(90, 0);
//		droneCraft.Create();
//		droneCraft.getLocation().set(200, 0, 2000);
//		droneCraft.setThrust(0.5f);
//		droneCraft.SetPull(1f);
//
//		radar.setReference(aircraft);
//		radar.addObjectToTrack(droneCraft);
//
//		grass = ObjLoader.loadObj(Gdx.files.internal("data/gridHills.obj")
//				.read());
//
//		grassTexture = mAssetManager
//				.get("data/greenchecker.png", Texture.class);
//		grassTexture.setFilter(TextureFilter.Linear,
//				TextureFilter.MipMapNearestNearest);
//
//		PerspectiveCamera cam = new PerspectiveCamera(45,
//				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
//		cam.far = 20000;
//
//		cam.position.set(aircraft.getDirection().x, 0, 30);
//		cam.direction.set(1, 0, 0);
//		cam.up.set(0, 0, 1);
//
//		mCamMan = new CameraMan(cam);
//		mCamMan.trackMode(aircraft);
//
//		font = new BitmapFont();
//		batch = new SpriteBatch();
//
//		marker = new PathMarker();
//		marker.create();
//
//		Gdx.input.setInputProcessor(ui);
//
//		resetInitialOrientation();
//
//		try {
//			MarkerManager.getInstance().registerMarker(aircraft);
//			MarkerManager.getInstance().registerMarker(droneCraft);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		m_skybox = new SkyBox();
//		m_skybox.setCameraDirection(cam.direction.cpy());
//		m_skybox.create(mAssetManager);
//	}
//
//	private void initializeAssetManager() {
//		mAssetManager = new AssetManager();
//		
//		mAssetManager.setErrorListener(new AssetErrorListener() {
//			@Override
//			public void error(String fileName, Class type, Throwable throwable) {
//				Gdx.app.log("AssetListener", "FAILED TO LOAD : " + fileName);
//			}
//		});
//		
//		TextureParameter para = new TextureLoader.TextureParameter();
//		para.genMipMaps = true;
//
//		mAssetManager.load("data/camo.jpg", Texture.class, para);
//		mAssetManager.load("data/checkerBigg.jpg", Texture.class, para);
//
//		para = new TextureLoader.TextureParameter();
//		para.genMipMaps = true;
//		mAssetManager.load("data/grass-texture.jpg", Texture.class, para);
//
//		para = new TextureLoader.TextureParameter();
//		para.genMipMaps = true;
//		mAssetManager.load("data/greenchecker.png", Texture.class, para);
//		mAssetManager.load("data/marker.png", Texture.class);
//
//		para = new TextureLoader.TextureParameter();
//		para.genMipMaps = true;
//		mAssetManager.load("data/skybox.png", Texture.class, para);
//
//		mAssetManager.load("data/ui/fire.png", Texture.class);
//		mAssetManager.load("data/ui/firePressed.png", Texture.class);
//		mAssetManager.load("data/ui/radar.png", Texture.class);
//		mAssetManager.load("data/ui/red.png", Texture.class);
//		mAssetManager.load("data/ui/sliderback.png", Texture.class);
//		mAssetManager.load("data/ui/sliderHandle.png", Texture.class);
//		mAssetManager.load("data/ui/switchCam.png", Texture.class);
//		mAssetManager.load("data/ui/switchCamPressed.png", Texture.class);
//		mAssetManager.load("data/ui/togMarkerPressed.png", Texture.class);
//		mAssetManager.load("data/ui/togMarker.png", Texture.class);
//
//		mAssetManager.finishLoading();
//
//		Texture.setAssetManager(mAssetManager);
//	}
//
//	private void createUI(AssetManager assetManager) {
//		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
//
//		slider = new Slider("slider");
//		slider.x = 0;
//		slider.y = 0;
//
//		radar = new Radar("radar");
//		radar.x = ui.width() - radar.width;
//		radar.y = 0;
//
//		TextureRegion pressedRegion = new TextureRegion(assetManager.get(
//				"data/ui/switchCamPressed.png", Texture.class), 0, 0, 32, 32);
//		TextureRegion normalRegion = new TextureRegion(assetManager.get("data/ui/switchCam.png", Texture.class), 0, 0, 32,
//				32);
//
//		cameraSwitchButton = new Button(new Button.ButtonStyle(new NinePatch(
//				pressedRegion), new NinePatch(normalRegion), new NinePatch(
//				normalRegion), 0f, 0f, 0f, 0.0f, null, Color.BLACK));
//
//		cameraSwitchButton.width = cameraSwitchButton.getPrefWidth();
//		cameraSwitchButton.height = cameraSwitchButton.getPrefHeight();
//
//		cameraSwitchButton.setClickListener(new ClickListener() {
//			@Override
//			public void click(Actor actor) {
//				actor.action(RotateBy.$(360, 0.4f));
//
//				if (mCamMan.getMode() == CameraMode.TRACKMODE) {
//					if (missile != null) {
//						mCamMan.fromToMode(missile, droneCraft);
//						mCamMan.mDistanceToTrack = 10;
//					} else {
//						mCamMan.fromToMode(aircraft, droneCraft);
//						mCamMan.mDistanceToTrack = 50;
//					}
//				} else {
//					mCamMan.trackMode(aircraft);
//					mCamMan.mDistanceToTrack = 50;
//				}
//			}
//		});
//
//		pressedRegion = new TextureRegion(assetManager.get("data/ui/firePressed.png", Texture.class), 0, 0, 32,
//				32);
//		normalRegion = new TextureRegion(assetManager.get("data/ui/fire.png", Texture.class), 0, 0, 32, 32);
//
//		fireButton = new Button(new Button.ButtonStyle(new NinePatch(
//				pressedRegion), new NinePatch(normalRegion), null, 0f, 0f, 0f,
//				0.0f, new BitmapFont(), Color.BLACK));
//
//		fireButton.setClickListener(new ClickListener() {
//			@Override
//			public void click(Actor actor) {
//				actor.action(RotateBy.$(360, 0.4f));
//
//				if (missile != null)
//					MarkerManager.getInstance().unregisterMarker(missile);
//				missile = aircraft.fireTo(droneCraft);
//				MarkerManager.getInstance().registerMarker(missile);
//
//				if (missile != null) {
//					mCamMan.fromToMode(missile, droneCraft);
//					mCamMan.mDistanceToTrack = 10;
//				}
//			}
//		});
//
//		pressedRegion = new TextureRegion(assetManager.get("data/ui/togMarkerPressed.png", Texture.class), 0,
//				0, 32, 32);
//		normalRegion = new TextureRegion(assetManager.get("data/ui/togMarker.png", Texture.class), 0, 0, 32,
//				32);
//
//		markerToggleButton = new Button(new Button.ButtonStyle(new NinePatch(
//				pressedRegion), new NinePatch(normalRegion), null, 0f, 0f, 0f,
//				0.0f, new BitmapFont(), Color.BLACK));
//
//		// markerToggleButton.x = cameraSwitchButton.x -
//		// markerToggleButton.width - 5;
//		// markerToggleButton.y = ui.top() - markerToggleButton.height;
//		markerToggleButton.setClickListener(new ClickListener() {
//			@Override
//			public void click(Actor actor) {
//				actor.action(RotateBy.$(360, 0.4f));
//				mShowMarker = !mShowMarker;
//			}
//		});
//
//		cameraSwitchButton.size(32, 32);
//		markerToggleButton.size(32, 32);
//		fireButton.size(32, 32);
//
//		Table group = new Table();
//
//		// group.width = Gdx.graphics.getWidth();
//		// group.height = Gdx.graphics.getHeight();
//
//		group.add(cameraSwitchButton).pad(2, 1, 2, 1);
//		group.add(markerToggleButton).pad(2, 1, 2, 1);
//		group.add(fireButton).pad(2, 1, 2, 1);
//
//		Table rootTable = new Table();
//		rootTable.debug();
//		rootTable.width = Gdx.graphics.getWidth();
//		rootTable.height = Gdx.graphics.getHeight();
//
//		rootTable.size(100, 100);
//
//		// fireButton.right();
//		rootTable.add(group).colspan(2).right().top().expandY();
//
//		rootTable.row().expandX().expandY();
//		rootTable.add(radar).left().bottom().pad(0, 2, 2, 0);
//		rootTable.add(slider).right().bottom().pad(0, 0, 2, 2);
//
//		rootTable.layout();
//
//		rootTable.debug("all");
//
//		ui.addActor(rootTable);
//	}
//
//	private void resetInitialOrientation() {
//		initialRoll = Gdx.input.getPitch();
//		initialPitch = Gdx.input.getRoll();
//	}
//
//	float mRenderTime = 0;
//
//	@Override
//	public void render() {
//
//		mRenderTime += Gdx.graphics.getDeltaTime();
//
//		float deltaTime = 15f / 1000f;
//
//		GL10 gl = Gdx.graphics.getGL10();
//
//		gl.glClearColor(.2f, .4f, 1, 1);
//		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
//
//		gl.glEnable(GL10.GL_DEPTH_TEST);
//		gl.glEnable(GL10.GL_LIGHTING);
//		gl.glEnable(GL10.GL_COLOR_MATERIAL);
//		gl.glEnable(GL10.GL_TEXTURE_2D);
//
//		gl.glEnable(GL10.GL_LIGHT0);
//		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightColor, 0);
//		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0);
//
//		float azimuth = Gdx.input.getAzimuth();
//		float pitch = Gdx.input.getPitch();
//		float roll = Gdx.input.getRoll();
//
//		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
//			pitch = -90 + initialRoll;
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
//			pitch = 90 + initialRoll;
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
//			roll = 90 + initialPitch;
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
//			roll = -90 + initialPitch;
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.MENU)) {
//			resetInitialOrientation();
//		}
//
//		aircraft.SetLean((pitch - initialRoll) / 45);
//		aircraft.SetPull((roll - initialPitch) / 30);
//
//		aircraft.setThrust(slider.getPosition());
//
//		aircraft.update(deltaTime);
//		droneCraft.update(deltaTime);
//
//		if (missile != null) {
//			missile.update(deltaTime);
//		}
//
//		mCamMan.update(deltaTime).apply(gl);
//
//		droneCraft.Render();
//		aircraft.Render();
//		if (missile != null)
//			missile.Render();
//
//		renderTerrain(gl);
//		renderMarker();
//
//		Vector3 direction = aircraft.getDirection().tmp();
//		direction.x = 0;
//		direction.nor();
//
//		double heading = Math.asin(direction.z);
//		heading = -heading;
//
//		// if (direction.y > 0)
//		heading = Math.atan2(direction.y, direction.z);
//		int dir = ((int) (heading * 180 / Math.PI + 360)) % 360;
//
//		float distance = aircraft.getLocation().dst(droneCraft.getLocation());
//
//		gl.glDisable(GL10.GL_DEPTH_TEST);
//		batch.begin();
//		String text = /*
//					 * "A :" + azimuth + "\nP :" + pitch + "\nR :" + roll +
//					 * "\nDeltaTime: " + deltaTime + "\nLean : " + (pitch -
//					 * initialRoll) / 45 + "\nPull : " + (roll - initialPitch) /
//					 * 30 + "\n" +
//					 */
//		"Position :" + aircraft.getLocation() + "\n" + "Heading :" + dir + "\n"
//				+ "Direction :" + aircraft.getDirection() + "\nDistance: "
//				+ distance;
//		font.drawMultiLine(batch, text, 20, Gdx.graphics.getHeight() - 5);
//		batch.end();
//
//		gl.glDisable(GL10.GL_LIGHTING);
//
//		ui.act(deltaTime);
//		ui.draw();
//
//		gl.glEnable(GL10.GL_LIGHTING);
//	}
//
//	private void renderMarker() {
//		if (mShowMarker) {
//			MarkerManager.getInstance().updateMarkers();
//			MarkerManager.getInstance().render();
//		}
//	}
//
//	private void renderTerrain(GL10 gl) {
//		gl.glPushMatrix();
//		gl.glScalef(10000, 10000, 10000);
//
//		grassTexture.bind();
//
//		grass.render(GL10.GL_TRIANGLES);
//
//		gl.glPopMatrix();
//	}
//
//	// private void setCameraMode() {
//	// if (cameraMode == 0) {
//	// Vector3 aircraftPos = aircraft.getLocation();
//	// Vector3 aircraftDir = aircraft.getDirection();
//	// float distanceToCraft = 50;
//	// cam.position.set(aircraftPos.x - aircraftDir.x * distanceToCraft,
//	// aircraftPos.y - aircraftDir.y * distanceToCraft,
//	// aircraftPos.z - aircraftDir.z * distanceToCraft);
//	// cam.direction.set(aircraftDir);
//	// } else if (cameraMode == 1) {
//	// final float maxDistance = 30;
//	//
//	// Vector3 camToaircraft = aircraft.getLocation().cpy()
//	// .sub(cam.position);
//	// Vector3 camToCraftVec = camToaircraft.cpy().nor();
//	//
//	// float distance = camToaircraft.len();
//	//
//	// if (distance > maxDistance) {
//	// cam.position.add(camToCraftVec.mul(distance - maxDistance));
//	// }
//	// cam.direction.set(camToCraftVec);
//	// } else if (cameraMode == 2) {
//	// float maxLenghth2 = 60 * 60;
//	//
//	// Vector3 delta = aircraft.getLocation().cpy().sub(cam.position);
//	//
//	// if (delta.len2() > maxLenghth2) {
//	// Vector3 newPos = aircraft.getLocation().cpy()
//	// .add(aircraft.getDirection().cpy().mul(40));
//	// cam.position.set(newPos);
//	// }
//	//
//	// Vector3 camDir = aircraft.getLocation().cpy().sub(cam.position)
//	// .nor();
//	// cam.direction.set(camDir);
//	// } else if (cameraMode == 3) {
//	// final float MaxDistance = 100;
//	// final float maxLenghth2 = MaxDistance * MaxDistance;
//	//
//	// Vector3 delta = aircraft.getLocation().cpy().sub(cam.position);
//	//
//	// if (delta.len2() > maxLenghth2) {
//	// float distance = delta.len();
//	// float deltaDistance = distance - MaxDistance;
//	//
//	// Vector3 camDir = aircraft.getLocation().tmp()
//	// .sub(cam.position).nor();
//	// cam.direction.set(camDir);
//	//
//	// cam.position.add(cam.direction.cpy().mul(deltaDistance));
//	// } else {
//	// Vector3 camDir = aircraft.getLocation().tmp()
//	// .sub(cam.position).nor();
//	// cam.direction.set(camDir);
//	// }
//	//
//	// } else if (cameraMode == 4) {
//	// if (missile == null) {
//	// cameraMode = 0;
//	// setCameraMode();
//	// return;
//	// }
//	// Vector3 missilePos = missile.getLocation();
//	// Vector3 missileDir = missile.getDirection().mul(-1);
//	//
//	// Vector3 missileToAirCraft = droneCraft.getLocation().tmp()
//	// .sub(missilePos).nor();
//	//
//	// float distanceToCraft = 20;
//	//
//	// cam.position.set(missilePos.x - missileDir.x
//	// * distanceToCraft, missilePos.y - missileDir.y
//	// * distanceToCraft, missilePos.z - missileDir.z
//	// * distanceToCraft);
//	//
//	// cam.position.set(missilePos.x - missileToAirCraft.x
//	// * distanceToCraft, missilePos.y - missileToAirCraft.y
//	// * distanceToCraft, missilePos.z - missileToAirCraft.z
//	// * distanceToCraft);
//	// cam.direction.set(missileToAirCraft);
//	// }
//	// }
//
//	@Override
//	public boolean keyDown(int keycode) {
//		return false;
//	}
//
//	@Override
//	public boolean keyTyped(char character) {
//		return false;
//	}
//
//	@Override
//	public boolean keyUp(int keycode) {
//		return false;
//	}
//
//	@Override
//	public boolean touchDown(int x, int y, int pointer, int newParam) {
//		if (ui.touchDown(x, y, pointer, newParam)) {
//			return true;
//		}
//
//		touchStartX = x;
//		touchStartY = y;
//
//		return false;
//	}
//
//	@Override
//	public boolean touchDragged(int x, int y, int pointer) {
//		/*
//		 * angleY += (x - touchStartX); angleX += (y - touchStartY); touchStartX
//		 * = x; touchStartY = y;
//		 */
//		if (ui.touchDragged(x, y, pointer)) {
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public boolean touchUp(int x, int y, int pointer, int button) {
//		if (ui.touchUp(x, y, pointer, button)) {
//			return true;
//		}
//		return false;
//	}
//
//	public boolean needsGL20() {
//		return false;
//	}
//
//	@Override
//	public boolean touchMoved(int x, int y) {
//		return false;
//	}
//
//	@Override
//	public boolean scrolled(int amount) {
//		return false;
//	}
//
//	@Override
//	public void resume() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void resize(int width, int height) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void pause() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void dispose() {
//		// TODO Auto-generated method stub
//
//	}
//
}
