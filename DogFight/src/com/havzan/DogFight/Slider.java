package com.test.myfirsttriangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public class Slider extends Group {

	Button mButton;
	Texture mButtonTexture;
	boolean mSliding = false;
	Vector2 lastDragPoint = new Vector2();
	float maxButtonY;
	private Image mBackImage;
	private float mPosition;
	
	
	public Slider(String name) {
		super(name);

		mButtonTexture = new Texture(Gdx.files.internal("data/sliderHandle.png"), true);
		Texture mBackTexture = new Texture(Gdx.files.internal("data/sliderback.png"), true);
		mBackImage = new Image("sliderback", mBackTexture);
		
		mButton = new Button("_slider_button", mButtonTexture);
		mButton.x = 0;
		mButton.y = 0;

		mBackImage.touchable = false;
		addActor(mBackImage);
		addActor(mButton);
		
		
		width = 32;
		height = 256;
		
		maxButtonY = height - mButton.height;
		
		mPosition = 0;
	}
	
	@Override
	protected boolean touchDown(float x, float y, int pointer) {
		mSliding = super.touchDown(x, y, pointer);
		
		if (mSliding)
		{
			lastDragPoint.x = x;
			lastDragPoint.y = y;
		}
		return mSliding;
	}
	@Override
	public void focus(Actor actor) {
		// TODO Auto-generated method stub
		focusedActor = actor;
		if (parent != null) parent.focus(actor == null ? null : this);
		
	}
	@Override
	protected boolean touchDragged(float x, float y, int pointer) {
		if(mSliding)
		{
			//mButton.y += lastDragPoint.x - x;
			float finalPos = mButton.y + y - lastDragPoint.y;
			lastDragPoint.y = y;
			lastDragPoint.x = x;
			mButton.y = Math.max(0, Math.min(maxButtonY, finalPos));
			mPosition = mButton.y / maxButtonY;
			return true;
		}
		
		return false;
	}
	
	@Override
	protected boolean touchUp(float x, float y, int pointer) {
		mSliding = false;
		focus(null);
		return false;
	}
	
	@Override
	protected void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		super.render(batch);
	}

	public void setPosition(float position) {
		this.mPosition = position;
		mButton.y = position * maxButtonY; 
	}

	public float getPosition() {
		return mPosition;
	}

}
