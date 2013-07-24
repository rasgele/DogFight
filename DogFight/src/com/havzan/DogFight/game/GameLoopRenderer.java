package com.havzan.dogfight.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.havzan.arena3D.CoordinateSystem;
import com.havzan.arena3D.Grid;
import com.havzan.dogfight.Assets;
import com.havzan.dogfight.game.model.Aircraft;
import com.havzan.dogfight.game.model.GameModel;
import com.havzan.dogfight.game.model.IModelObject;
import com.havzan.dogfight.game.model.Missile;
import com.havzan.dogfight.game.model.ModelObjectChangeLister;

public class GameLoopRenderer {
	
	GameModel model;
	
	HashMap<IModelObject, ModelInstance> modelObj2Instance = new HashMap<IModelObject, ModelInstance>();
	ArrayList<ModelInstance> modelInstances = new ArrayList<ModelInstance>();
	private Camera camera;
	private float fieldOfView = 67;

	private Lights lights;

	private ModelBatch batch;

	private CoordinateSystem coordinateSystem;

	private Grid grid;
	
	public GameLoopRenderer(GameModel model){
		assert model != null;
		this.model = model;
		model.addListener(new ModelObjectChangeLister() {
			
			@Override
			public void missileRemoved(Missile missile) {
				removeModelObject(missile);
			}
			
			@Override
			public void missileAdded(Missile missile) {
				addMissile(missile);
			}
			
			@Override
			public void aircraftRemoved(Aircraft aircraft) {
				removeModelObject(aircraft);
			}
			
			@Override
			public void aircraftAdded(Aircraft aircraft) {
				addAircraft(aircraft);
				
			}
		});
	}
	
	private void removeModelObject(IModelObject object) {
		ModelInstance instance = modelObj2Instance.remove(object);
		modelInstances.remove(instance);		
	}

	public void create(){       
        coordinateSystem = new CoordinateSystem();
        coordinateSystem.setSize(8f);
        coordinateSystem.create();
		
		lights = new Lights();
        lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
        batch = new ModelBatch();
		
		for (Aircraft a : model.getAircrafts()){
			addAircraft(a);
		}
		
		for (Missile m : model.getMissiles()){
			addMissile(m);
		}
		
		grid = new Grid();
		grid.create(100, 100, Color.GREEN);
		ModelInstance instance = new ModelInstance(grid.getModel());
		modelInstances.add(instance);
		//Model terrain = TerrainModelCreator.createModel(model.terrain);
		//ModelInstance terrainInstance = new ModelInstance(terrain);
		//modelInstances.add(terrainInstance);
		

		batch = new ModelBatch();
	}

	private void addMissile(Missile m) {
		// TODO Types of missiles read from GameModel??
		Model model = Assets.getModelById("missile");
		ModelInstance instance = new ModelInstance(model, m.getOrientation());
		modelObj2Instance.put(m, instance);
		modelInstances.add(instance);
	}

	private void addAircraft(Aircraft a) {
		Model model = Assets.getModelById(a.getTypeId());
		ModelInstance instance = new ModelInstance(model, a.getOrientation());
		modelObj2Instance.put(a, instance);
		modelInstances.add(instance);
	}
	void render(float deltaTime)
	{
		assert camera != null;
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
 
//        camController.update();
//        camera.update();
        
        updateModelInstances();

		batch.begin(camera);

		//grid.render(batch);
		batch.render(modelInstances, lights);
		
		coordinateSystem.render(batch);

		batch.end();
		
	}	
	
	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
//		camController.camera = camera;
	}
	
	private void updateModelInstances(){
		for (Entry<IModelObject, ModelInstance> entry : modelObj2Instance.entrySet()){
			entry.getValue().transform.set(entry.getKey().getOrientation());
		}
	}
	
}
