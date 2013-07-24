package com.havzan.DogFight.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TableToolkit;
import com.havzan.DogFight.DogFightGame;
import com.havzan.DogFight.game.model.GameModel;
import com.havzan.DogFight.game.ui.GameUI;

public class GameLoopScreen implements Screen{

	private DogFightGame game;
	GameModel gameModel;
	GameLoopRenderer renderer;
	GameWorld world;
	private GameUI ui;

	public GameLoopScreen(DogFightGame game, GameModel model){
		this.game = game;
		this.gameModel = model;
		this.renderer = new GameLoopRenderer(model);
		this.world = new GameWorld(model, renderer);
		this.ui = new GameUI(world);
		ui.setOwner(model.getPlayer());
		create();
		world.setUI(this.ui);
	}
	
	private void create()
	{
		renderer.create();
		world.create();
	}
	
	@Override
	public void render(float delta) {
		world.update(delta);
		world.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
