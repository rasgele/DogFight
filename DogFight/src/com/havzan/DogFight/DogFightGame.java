package com.havzan.DogFight;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateBy;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class DogFightGame implements ApplicationListener, InputProcessor{
	PerspectiveCamera cam;
	Mesh mesh;
	Mesh grass;
	Texture texture;
	Texture grassTexture;
	float angleY = 0;
	float angleX = 0;
	float[] lightColor = { 1, 1, 1, 0 };
	float[] lightPosition = { 2, 5, 10, 0 };
	float touchStartX = 0;
	float touchStartY = 0;
	Aircraft aircraft;
	Missile missile;

	Aircraft droneCraft;

	BitmapFont font;
	SpriteBatch batch;
	PathMarker marker;

	float initialRoll;
	float initialPitch;

	int cameraMode = 0;
	private SkyBox m_skybox;
	static final int MAX_CAM_MODE = 5;

	Stage ui;
	Slider slider;
	Button cameraSwitchButton;
	Button markerToggleButton;

	boolean mShowMarker = true;
	private Button fireButton;
	private Radar radar;

	@Override
	public void create() {
		createUI();

		aircraft = new Aircraft();

		aircraft.Create();
		aircraft.getLocation().set(20, 0, 0);

		droneCraft = new Aircraft(90, 0);

		droneCraft.Create();
		droneCraft.getLocation().set(20, 0, -100);
		
		droneCraft.setThrust(0f);

		droneCraft.SetPull(1);
		
		radar.setReference(aircraft);
		
		radar.addObjectToTrack(droneCraft);

		grass = ObjLoader.loadObj(Gdx.files.internal("data/grass.obj").read());
		grassTexture = new Texture(
				Gdx.files.internal("data/grass-texture.jpg"), true);
		grassTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);

		cam = new PerspectiveCamera();

		cam.far = 2000;

		cam.position.set(aircraft.getDirection().x, 0, 30);
		cam.direction.set(-0, 0, -1);
		cam.up.set(1, 0, 0);

		font = new BitmapFont();
		batch = new SpriteBatch();

		marker = new PathMarker();
		marker.create();

		Gdx.input.setInputProcessor(this);

		resetInitialOrientation();
		
		try {
			MarkerManager.getInstance().registerMarker(aircraft);
			MarkerManager.getInstance().registerMarker(droneCraft);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m_skybox = new SkyBox();
		m_skybox.setCameraDirection(cam.direction);
		m_skybox.create();
	}

	private void createUI() {
		ui = new Stage(480, 320, true);

		slider = new Slider("slider");
		slider.x = 0;
		slider.y = 0;
		
		radar = new Radar("radar");
		radar.x = ui.width() - radar.width;
		radar.y = 0;
		
		LinearGroup group = new LinearGroup("linear_hor", 32 * 3, 32,
				LinearGroupLayout.Horizontal);

		TextureRegion pressedRegion = new TextureRegion(new Texture(
				Gdx.files.internal("data/ui/switchCamPressed.png"), true), 0,
				0, 32, 32);
		TextureRegion normalRegion = new TextureRegion(new Texture(
				Gdx.files.internal("data/ui/switchCam.png"), true), 0, 0, 32,
				32);

		cameraSwitchButton = new Button("btn", normalRegion, pressedRegion);

		// cameraSwitchButton.x = ui.right() - cameraSwitchButton.width;
		// cameraSwitchButton.y = ui.top() - cameraSwitchButton.height;
		cameraSwitchButton.clickListener = new ClickListener() {
			@Override
			public void clicked(Button button) {
				button.action(RotateBy.$(360, 0.4f));
				cameraMode = (cameraMode + 1) % MAX_CAM_MODE;
				setCameraMode();

			}
		};

		pressedRegion = new TextureRegion(new Texture(
				Gdx.files.internal("data/ui/firePressed.png"), true), 0, 0, 32,
				32);
		normalRegion = new TextureRegion(new Texture(
				Gdx.files.internal("data/ui/fire.png"), true), 0, 0, 32, 32);

		fireButton = new Button("btnFire", normalRegion, pressedRegion);

		fireButton.clickListener = new ClickListener() {
			@Override
			public void clicked(Button button) {
				button.action(RotateBy.$(360, 0.4f));

				if (missile != null)
					MarkerManager.getInstance().unregisterMarker(missile);
				missile = aircraft.fireTo(droneCraft);
				MarkerManager.getInstance().registerMarker(missile);
				cameraMode = 4;
				setCameraMode();

			}
		};

		pressedRegion = new TextureRegion(new Texture(
				Gdx.files.internal("data/ui/togMarkerPressed.png"), true), 0,
				0, 32, 32);
		normalRegion = new TextureRegion(new Texture(
				Gdx.files.internal("data/ui/togMarker.png"), true), 0, 0, 32,
				32);

		markerToggleButton = new Button("btn", normalRegion, pressedRegion);

		// markerToggleButton.x = cameraSwitchButton.x -
		// markerToggleButton.width - 5;
		// markerToggleButton.y = ui.top() - markerToggleButton.height;
		markerToggleButton.clickListener = new ClickListener() {
			@Override
			public void clicked(Button button) {
				button.action(RotateBy.$(360, 0.4f));
				mShowMarker = !mShowMarker;
			}
		};

		group.addActor(cameraSwitchButton);
		group.addActor(markerToggleButton);
		group.addActor(fireButton);
		group.x = ui.right() - group.width;
		group.y = ui.top() - group.height;

		ui.addActor(slider);
		ui.addActor(radar);
		ui.addActor(group);
		// ui.addActor(markerToggleButton);
	}

	private void resetInitialOrientation() {
		initialRoll = Gdx.input.getPitch();
		initialPitch = Gdx.input.getRoll();
	}

	@Override
	public void render() {

		float deltaTime = Gdx.graphics.getDeltaTime();

		GL10 gl = Gdx.graphics.getGL10();

		gl.glClearColor(.2f, .4f, 1, 1);
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		setCameraMode();
		cam.update();

		aircraft.setThrust(slider.getPosition());

		// m_skybox.render(cam);

		cam.setMatrices();

		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightColor, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0);

		float azimuth = Gdx.input.getAzimuth();
		float pitch = Gdx.input.getPitch();
		float roll = Gdx.input.getRoll();

		if (Gdx.input.isKeyPressed(Input.Keys.KEYCODE_DPAD_LEFT)) {
			pitch = 90 + initialRoll;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.KEYCODE_DPAD_RIGHT)) {
			pitch = -90 + initialRoll;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.KEYCODE_DPAD_UP)) {
			roll = 90 + initialPitch;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.KEYCODE_DPAD_DOWN)) {
			roll = -90 + initialPitch;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.KEYCODE_MENU)) {
			resetInitialOrientation();
		}

		aircraft.SetLean((pitch - initialRoll) / 45);
		aircraft.SetPull((roll - initialPitch) / 30);

		aircraft.Update(deltaTime);
		aircraft.Render();

		droneCraft.Update(deltaTime);
		droneCraft.Render();

		if (missile != null) {
			missile.Update(deltaTime);
			missile.Render();
		}

		renderTerrain(gl);
		renderMarker();

		Vector3 direction = aircraft.getDirection().tmp();
		direction.x = 0;
		direction.nor();

		double heading = Math.asin(direction.z);
		heading = -heading;

		// if (direction.y > 0)
		heading = Math.atan2(direction.y, direction.z);
		int dir = ((int) (heading * 180 / Math.PI + 360)) % 360;
		
		float distance = aircraft.getLocation().dst(droneCraft.getLocation());
		

		gl.glDisable(GL10.GL_DEPTH_TEST);
		batch.begin();
		String text = /*
					 * "A :" + azimuth + "\nP :" + pitch + "\nR :" + roll +
					 * "\nDeltaTime: " + deltaTime + "\nLean : " + (pitch -
					 * initialRoll) / 45 + "\nPull : " + (roll - initialPitch) /
					 * 30 + "\n" +
					 */
		"Position :" + aircraft.getLocation() + "\n" + "Heading :" + dir + "\n"
				+ "Direction :" + aircraft.getDirection() + "\nDistance: " +
				distance;
		font.drawMultiLine(batch, text, 20, Gdx.graphics.getHeight() - 5);
		batch.end();

		ui.act(deltaTime);
		ui.render();
	}

	private void renderMarker() {
		if (mShowMarker) {
			MarkerManager.getInstance().updateMarkers();
			MarkerManager.getInstance().render();
		}
	}

	private void renderTerrain(GL10 gl) {
		gl.glPushMatrix();
		gl.glScalef(10000, 10000, 10000);

		grassTexture.bind();

		grass.render(GL.GL_TRIANGLES);

		gl.glPopMatrix();
	}

	private void setCameraMode() {
		if (cameraMode == 0) {
			Vector3 aircraftPos = aircraft.getLocation();
			Vector3 aircraftDir = aircraft.getDirection();
			float distanceToCraft = 10;
			cam.getPosition().set(
					aircraftPos.x - aircraftDir.x * distanceToCraft,
					aircraftPos.y - aircraftDir.y * distanceToCraft,
					aircraftPos.z - aircraftDir.z * distanceToCraft);
			cam.getDirection().set(aircraftDir);
			cam.update();
		} else if (cameraMode == 1) {
			final float maxDistance = 30;

			Vector3 camToaircraft = aircraft.getLocation().cpy()
					.sub(cam.getPosition());
			Vector3 camToCraftVec = camToaircraft.cpy().nor();

			float distance = camToaircraft.len();

			if (distance > maxDistance) {
				cam.getPosition()
						.add(camToCraftVec.mul(distance - maxDistance));
			}
			cam.getDirection().set(camToCraftVec);
		} else if (cameraMode == 2) {
			float maxLenghth2 = 60 * 60;

			Vector3 delta = aircraft.getLocation().cpy().sub(cam.getPosition());

			if (delta.len2() > maxLenghth2) {
				Vector3 newPos = aircraft.getLocation().cpy()
						.add(aircraft.getDirection().cpy().mul(40));
				cam.getPosition().set(newPos);
			}

			Vector3 camDir = aircraft.getLocation().cpy()
					.sub(cam.getPosition()).nor();
			cam.getDirection().set(camDir);
		} else if (cameraMode == 3) {
			final float MaxDistance = 25;
			final float maxLenghth2 = MaxDistance * MaxDistance;

			Vector3 delta = aircraft.getDirection().cpy().sub(cam.getPosition());

			if (delta.len2() > maxLenghth2) {
				float distance = delta.len();
				float deltaDistance = distance - MaxDistance;

				Vector3 camDir = aircraft.getDirection().cpy()
						.sub(cam.getPosition()).nor();
				cam.getDirection().set(camDir);

				cam.getPosition().add(
						cam.getDirection().cpy().mul(deltaDistance));
			} else {
				Vector3 camDir = aircraft.getDirection().cpy()
						.sub(cam.getPosition()).nor();
				cam.getDirection().set(camDir);
			}

		} else if (cameraMode == 4) {
			Vector3 missilePos = missile.getLocation();
			// Vector3 missileDir = missile.getDirection();

			Vector3 missileToAirCraft = droneCraft.getLocation().cpy()
					.sub(missilePos).nor();

			float distanceToCraft = 10;

			cam.getPosition().set(
					missilePos.x - missileToAirCraft.x * distanceToCraft,
					missilePos.y - missileToAirCraft.y * distanceToCraft,
					missilePos.z - missileToAirCraft.z * distanceToCraft);
			cam.getDirection().set(missileToAirCraft);
			cam.update();

		}

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int newParam) {
		if (ui.touchDown(x, y, pointer, newParam)) {
			return true;
		}

		touchStartX = x;
		touchStartY = y;

		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		/*
		 * angleY += (x - touchStartX); angleX += (y - touchStartY); touchStartX
		 * = x; touchStartY = y;
		 */
		if (ui.touchDragged(x, y, pointer)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (ui.touchUp(x, y, pointer, button)) {
			return true;
		}
		return false;
	}

	public boolean needsGL20() {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
