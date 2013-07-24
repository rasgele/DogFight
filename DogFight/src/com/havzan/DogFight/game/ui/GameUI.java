package com.havzan.dogfight.game.ui;

import java.util.ArrayList;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.havzan.dogfight.Assets;
import com.havzan.dogfight.IWorldObject;
import com.havzan.dogfight.game.GameWorld;
import com.havzan.dogfight.game.model.Aircraft;
import com.havzan.dogfight.game.model.GameModel;
import com.havzan.dogfight.game.model.IModelObject;
import com.havzan.dogfight.game.model.Missile;
import com.havzan.dogfight.game.model.ModelObjectChangeLister;
import com.havzan.dogfight.game.ui.HeadUpDisplay.HeadUpDisplayStyle;
import com.havzan.dogfight.game.ui.Radar.RadarStyle;

public class GameUI {	
	private GameWorld world;
	private Stage stage;
	private Table root;
	private Button markerToggleButton;
	private Button camSwitchBtn;
	private Slider timeSlider;
	private Slider thrustSlider;
	private Button fireButton;

	public Aircraft controlled;
	private Radar radar;
	private HeadUpDisplay hud;
	private List itemList;
	
	public GameUI(final GameWorld world) {
		this.world = world;		
		create();
		this.world.getModel().addListener(new ModelObjectChangeLister() {
			
			@Override
			public void missileRemoved(Missile missile) {
				updateObjectList();
				radar.removeObjectToTrack(missile);
			}
			
			@Override
			public void missileAdded(Missile missile) {
				updateObjectList();
				radar.addObjectToTrack(missile);
			}
			
			@Override
			public void aircraftRemoved(Aircraft aircraft) {
				updateObjectList();
				hud.removeTrackingData(aircraft);
				radar.removeObjectToTrack(aircraft);
			}
			
			@Override
			public void aircraftAdded(Aircraft aircraft) {
				updateObjectList();				
				radar.addObjectToTrack(aircraft);
			}
		});
	}
	
	public InputProcessor getInputProcessor(){
		return stage;
	}
	private Table createToolsGroup(){
		Skin skin = Assets.getSkinUI();
		
		camSwitchBtn = new Button(skin.get("switchCam", ButtonStyle.class));
		markerToggleButton = new Button(skin.get("toggleMarker", ButtonStyle.class));
		timeSlider = new Slider(0, 2, 0.1f, false, skin);
		timeSlider.setWidth(256);	
		timeSlider.setValue(0.1f);
		world.setSimulationSpeed(timeSlider.getValue());

		thrustSlider = new Slider(0, 1, 0.01f, true, skin);
		thrustSlider.setHeight(256);
		fireButton = new Button(skin.get("fire", ButtonStyle.class));
		
		
		camSwitchBtn.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				world.switchCamera();
			}
		});

		markerToggleButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				world.switchCamera();
			}
		});
		
		timeSlider.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				world.setSimulationSpeed(timeSlider.getValue());			
			}});	
		
		fireButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Aircraft aircraft = hud.getOwner();
				world.fireMissile(aircraft, aircraft.targetInfo.getTarget());
			}});
		
		thrustSlider.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				world.getModel().getPlayer().setThrust(thrustSlider.getValue());			
			}});
		
		return root;
	}
	
	Button getButton(){
		Button b = new Button(Assets.getSkinUI().get("fire", ButtonStyle.class));
		return b;
	}
	
	
	public void create(){
		Skin skin = Assets.getSkinUI();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		root = new Table().debug();	
		
		root.setFillParent(true);
		root.row().top().expandX().expandY();
		
		Table tl = new Table().debug();
		Table tr = new Table().debug();
		Table bl = new Table().debug();
		Table br = new Table().debug();
		
		createToolsGroup();
		Table toolsTable = new Table().debug().pad(4);
		toolsTable.left().setFillParent(true);
		tl.row().expandX();
		tl.add(toolsTable).left();
		toolsTable.add(this.camSwitchBtn).left();
		toolsTable.add(this.markerToggleButton).left();
		toolsTable.add(this.timeSlider).expandX().right().width(256);
		
		tr.add(getButton()).pad(4);


		ArrayList<IModelObject> worldModels = new ArrayList<IModelObject>();
		worldModels.addAll(world.getModel().getAircrafts());
		worldModels.addAll(world.getModel().getMissiles());
		
		itemList = new List(worldModels.toArray(), skin);
		itemList.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				List list = (List)actor;
				world.setSelectedObject(getObjectByName(list.getSelection()));
			}
		});
		
		radar = new Radar(Assets.getSkinUI().get(RadarStyle.class));
		radar.setReference(world.getModel().getPlayer());
		
		radar.init(world.getModel());
		
		bl.add(itemList);
		bl.row();
		bl.add(radar);
		
		
		Table controlTool = new Table().debug();
		
		controlTool.add(thrustSlider).expandY().fillY().top().pad(4);
		controlTool.row();
		controlTool.add(fireButton).pad(4);

		br.add(controlTool).expandY().fillY();
		
		root.add(tl).left().expandX().fillX();
		root.add(tr).right().top();
		
		root.row().expandY();
		
		root.add(bl).left().bottom();
		root.add(br).right().expandY().fillY();
		
		
		root.invalidateHierarchy();
		
		hud = new HeadUpDisplay(skin.get(HeadUpDisplayStyle.class), world);
		hud.size(stage.getWidth(), stage.getHeight());
		hud.addActor(root);
		
		stage.addActor(hud);
		//stage.addActor(root);
		
		//hud.toFront();
		
		System.out.println("Stage  W :" + stage.getWidth() + " H: " + stage.getHeight());
		System.out.println("Root x :" + root.getX() + " y : " + root.getY() +
				"W :" + root.getWidth() + " H: " + root.getHeight());
//		System.out.println("tools x :" + tools.getX() + " y : " + tools.getY() +
//				"W :" + tools.getWidth() + " H: " + tools.getHeight());
		
	}
	
	protected IModelObject getObjectByName(String name) {
		Aircraft aircraft = world.getModel().getAircraftByName(name);
		return aircraft != null ? aircraft : world.getModel().getMissileByName(name);
	}
	
	private void updateHudData(){
		Camera cam = world.getCamDirector().getCamera();
		GameModel model = world.getModel();
		Aircraft player = model.getPlayer();
		Aircraft lockedAircraft = player.targetInfo.getTarget();
		Vector3 playerPos = player.getLocation(new Vector3());
		
		for (Aircraft a : world.getModel().getAircrafts()){
			if (a == player)
				continue;
			HUDTrackingData data = hud.getTrackingDataOf(a);
			if (data == null){
				data = new HUDTrackingData(a);
				hud.addTrackingData(data);
			}
			data.mIsLocked = data.aircraft == lockedAircraft;
			HUDCalculator.setTrackingData(cam, data, playerPos);
		}
	}

	public void render(float deltaSecs){
		updateHudData();
		root.debug();
		stage.draw();
		root.debugCell();
		//Table.drawDebug(stage); 
	}

	private void updateObjectList() {
		ArrayList<IModelObject> worldModels = new ArrayList<IModelObject>();
		worldModels.addAll(world.getModel().getAircrafts());
		worldModels.addAll(world.getModel().getMissiles());	
		
		if (itemList != null)
			itemList.setItems(worldModels.toArray());
	}

	public void setOwner(Aircraft owner) {
		hud.setOwner(owner);
		
	}

}
