package com.havzan.dogfight.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.havzan.dogfight.game.cameras.CameraDirector;
import com.havzan.dogfight.game.model.Aircraft;
import com.havzan.dogfight.game.model.GameModel;
import com.havzan.dogfight.game.model.IModelObject;
import com.havzan.dogfight.game.model.Missile;
import com.havzan.dogfight.game.model.MissileParams;
import com.havzan.dogfight.game.model.ModelObjectChangeLister;
import com.havzan.dogfight.game.physics.AircraftSolver;
import com.havzan.dogfight.game.physics.MissileSolver;
import com.havzan.dogfight.game.physics.MissileSolver.UpdateResult;
import com.havzan.dogfight.game.physics.SolverFactory;
import com.havzan.dogfight.game.pilots.RoundCornerPilot;
import com.havzan.dogfight.game.ui.GameUI;

public class GameWorld {
	private CameraDirector camDirector;
	// private CameraMan camManager;
	private ChainedInputProcessor chain;
	private RoundCornerPilot pilot;
	private RoundCornerPilot pilot2;
	private PlayerController playerController;
	private GameLoopRenderer renderer;
	private float speedMultiplier;
	private GameUI ui;
	HashMap<Aircraft, AircraftSolver> aircraftSolvers = new HashMap<Aircraft, AircraftSolver>();
	HashMap<Missile, MissileSolver> missileSolvers = new HashMap<Missile, MissileSolver>();
	GameModel model;
	static int aircraftCounter = 0;
	static int missileCounter = 0;

	public GameWorld(GameModel gameModel, GameLoopRenderer renderer) {
		this.model = gameModel;
		this.renderer = renderer;
		this.model.addListener(new ModelObjectChangeLister() {

			@Override
			public void missileRemoved(Missile missile) {
				// Remove op done on update iteration
				// missileSolvers.remove(missile);
			}

			@Override
			public void missileAdded(Missile missile) {
				missileSolvers.put(missile, getSolver(missile));

			}

			@Override
			public void aircraftRemoved(Aircraft aircraft) {
				// Remove op done on update iteration
				// aircraftSolvers.remove(aircraft);

			}

			@Override
			public void aircraftAdded(Aircraft aircraft) {
				aircraftSolvers.remove(aircraft);
			}
		});
	}

	public void create() {
		for (Aircraft aircraft : model.getAircrafts()) {
			aircraftSolvers.put(aircraft, getSolver(aircraft));
		}

		for (Missile missile : model.getMissiles()) {
			missileSolvers.put(missile, getSolver(missile));
		}

		pilot = new RoundCornerPilot(this, model.getAircrafts().get(
				model.getAircrafts().size() - 1));
		pilot2 = new RoundCornerPilot(this, model.getAircrafts().get(
				model.getAircrafts().size() - 2));

		playerController = new PlayerController();
		playerController.setPlayer(model.getPlayer());

		camDirector = new CameraDirector();

		//
		// camManager = new CameraMan(Gdx.graphics.getWidth(),
		// Gdx.graphics.getHeight());
		camDirector.setTrackObj(model.getMissiles().get(0));
		// camManager.trackMode(model.getPlayer());
		renderer.setCamera(camDirector.getCamera());

		chain = new ChainedInputProcessor();
		chain.processors.add(camDirector.getUIController());
		Gdx.input.setInputProcessor(chain);
	}

	public Missile fireMissile(IModelObject from, IModelObject to) {
		Missile m = new Missile("Missile " + missileCounter++,
				new MissileParams(), from, to);
		m.setOrientation(from.getOrientation());
		model.addMissile(m);
		missileSolvers.put(m, getSolver(m));

		return m;
	}

	public CameraDirector getCamDirector() {
		return camDirector;
	}

	public GameModel getModel() {
		return model;
	}

	public void render(float delta) {
		float modifiedTime = delta * speedMultiplier;
		renderer.render(modifiedTime);
		ui.render(modifiedTime);

	}

	public void setSimulationSpeed(float speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}

	public void setTracking(boolean track, IModelObject objectToTrack) {

	}

	public void setUI(GameUI ui) {
		if (this.ui != null)
			chain.processors.remove(this.ui.getInputProcessor());
		this.ui = ui;
		chain.processors.add(0, this.ui.getInputProcessor());
	}

	public void switchCamera() {
		camDirector.switchNextCam();
		renderer.setCamera(camDirector.getCamera());
	}

	public void update(float deltaSecs) {
		deltaSecs *= speedMultiplier;
		playerController.updateControls();

		pilot.update(deltaSecs);
		pilot2.update(deltaSecs);

		for (Entry<Aircraft, AircraftSolver> e : aircraftSolvers.entrySet()) {
			e.getValue().update(e.getKey(), deltaSecs);
		}

		for (Iterator<Entry<Missile, MissileSolver>> it = missileSolvers
				.entrySet().iterator(); it.hasNext();) {
			Entry<Missile, MissileSolver> e = it.next();
			UpdateResult result = e.getValue().update(e.getKey(), deltaSecs);
			switch (result) {
			case MissileHit:
				it.remove();
				aircraftSolvers.remove((Aircraft) e.getKey().target);
				model.removeMissile(e.getKey());
				model.removeAircraft((Aircraft) e.getKey().target);
				break;
			case MissileLostContact:
				model.removeMissile(e.getKey());
			default:
				break;
			}
		}

		camDirector.update(deltaSecs);
	}

	private AircraftSolver getSolver(Aircraft aircraft) {
		return SolverFactory.getAircraftSolver(aircraft.getTypeId());
	}

	private MissileSolver getSolver(Missile missile) {
		// TODO Get missile id ?
		return SolverFactory.getMissileSolver("missile");
	}

	public void setSelectedObject(IModelObject objectByName) {
		camDirector.setTrackObj(objectByName);
	}

	public void setLockOnFoe(Aircraft player, Aircraft aircraft) {
		player.targetInfo.setTarget(aircraft);
	}

}
