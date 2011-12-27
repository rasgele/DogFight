package com.havzan.DogFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;

public class Slider extends Group {

	Button mButton;
	TextureRegion mButtonTexture;
	boolean mSliding = false;
	Vector2 lastDragPoint = new Vector2();
	float maxButtonY;
	private Image mBackImage;
	private float mPosition;
	
	
	public Slider(String name) {
		super(name);

		mButtonTexture = new TextureRegion(new Texture(Gdx.files.internal("data/ui/sliderHandle.png"), true));
		TextureRegion mBackTexture = new TextureRegion(new Texture(Gdx.files.internal("data/ui/sliderback.png"), true), 0, 0, 32, 256);
		mBackImage = new Image("sliderback", mBackTexture);
		
		mButton = new Button(new Button.ButtonStyle(new NinePatch(mButtonTexture), new NinePatch(mButtonTexture), null, 0 ,0,0,0,null, Color.BLACK));
		mButton.width = 32;
		mButton.height = 32;
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
	public boolean touchDown(float x, float y, int pointer) {
		mSliding = super.touchDown(x, y, pointer);
		
		if (mSliding)
		{
			lastDragPoint.x = x;
			lastDragPoint.y = y;
		}
		return mSliding;
	}
	@Override
	public void focus(Actor actor, int pointer) {
		// TODO Auto-generated method stub
		if (parent != null) parent.focus(actor == null ? null : this, 0);
		
	}
	@Override
	public void touchDragged(float x, float y, int pointer) {
		if(mSliding)
		{
			//mButton.y += lastDragPoint.x - x;
			float finalPos = mButton.y + y - lastDragPoint.y;
			lastDragPoint.y = y;
			lastDragPoint.x = x;
			mButton.y = Math.max(0, Math.min(maxButtonY, finalPos));
			mPosition = mButton.y / maxButtonY;
			return;
		}
		
		return;
	}
	
	@Override
	public void touchUp(float x, float y, int pointer) {
		mSliding = false;
		focus(null, 0);
		return;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
	}

	public void setPosition(float position) {
		this.mPosition = position;
		mButton.y = position * maxButtonY; 
	}

	public float getPosition() {
		return mPosition;
	}

}
