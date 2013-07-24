package com.havzan.DogFight;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class ButtonStyleHelper {
	private ButtonStyleHelper(){}
	
	public static ButtonStyle createDefaultButtonStyle(String pressed, String normal) {
		return createDefaultButtonStyle(Assets.getAsset(pressed, Texture.class),
				Assets.getAsset(normal, Texture.class));
	}

	public static ButtonStyle createDefaultButtonStyle(Texture pressed, Texture normal) {
		TextureRegion regPressed = new TextureRegion(pressed, 0, 0, pressed.getWidth(), pressed.getHeight());
		TextureRegion regNormal = new TextureRegion(normal, 0, 0, normal.getWidth(), normal.getHeight());
		NinePatch down = new NinePatch(regPressed);
		NinePatch up = new NinePatch(regNormal);
		NinePatchDrawable drUp = new NinePatchDrawable(up);
		NinePatchDrawable drDown = new NinePatchDrawable(down);
		return new Button.ButtonStyle(drDown, drUp, null);
		//return new Button.ButtonStyle(down, up, null, 0f, 0f, 0f, 0.0f, null,
			//	Color.BLACK);
	}	
	
}
