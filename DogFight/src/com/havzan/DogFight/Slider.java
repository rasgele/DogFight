package com.havzan.DogFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class Slider extends Group {

	Button mButton;
	TextureRegion mButtonTexture;
	boolean mSliding = false;
	Vector2 lastDragPoint = new Vector2();
	float maxButtonY;
	private Image mBackImage;
	private float mPosition;
	
	
	public Slider(String name) {
		super();

		mButtonTexture = new TextureRegion(new Texture(Gdx.files.internal("data/ui/sliderHandle.png"), true));
		TextureRegion mBackTexture = new TextureRegion(new Texture(Gdx.files.internal("data/ui/sliderback.png"), true), 0, 0, 32, 256);
		mBackImage = new Image(mBackTexture);
		
		mButton = new Button(new Button.ButtonStyle(new NinePatchDrawable(new NinePatch(mButtonTexture)),
				new NinePatchDrawable(new NinePatch(mButtonTexture)), null));
		mButton.setWidth(32);
		mButton.setHeight(32);
		mButton.setX(0);
		mButton.setY(0);

		mBackImage.setTouchable(Touchable.disabled);
		
		addActor(mBackImage);
		addActor(mButton);
		
		
		setWidth(32);
		setHeight(256);
		
		maxButtonY = getHeight() - mButton.getHeight();
		
		mPosition = 0;
		
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)  {
				mSliding = true;//super.touchDown(x, y, pointer);
				
				if (mSliding)
				{
					lastDragPoint.x = x;
					lastDragPoint.y = y;
				}
				return mSliding;
			}
//			public void focus(Actor actor, int pointer) {
//				// TODO Auto-generated method stub
//				if (parent != null) parent.focus(actor == null ? null : this, 0);
//				
//			}
			@Override
			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				if(mSliding)
				{
					//mButton.y += lastDragPoint.x - x;
					float finalPos = mButton.getY() + y - lastDragPoint.y;
					lastDragPoint.y = y;
					lastDragPoint.x = x;
					mButton.setY(Math.max(0, Math.min(maxButtonY, finalPos)));
					mPosition = mButton.getY() / maxButtonY;
					return;
				}
				
				return;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button){
				mSliding = false;
				//focus(null, 0);
				return;
			}
		}
		);
	}
	

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
	}

	public void setPosition(float position) {
		this.mPosition = position;
		mButton.setY(position * maxButtonY); 
	}

	public float getPosition() {
		return mPosition;
	}

}
