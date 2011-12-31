package com.havzan.DogFight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;

public class ButtonStyleHelper {
	private ButtonStyleHelper(){}
	
	public static ButtonStyle createDefaultButtonStyle(String pressed, String normal) {
		return createDefaultButtonStyle(Assets.getTexture(pressed),
				Assets.getTexture(normal));
	}

	public static ButtonStyle createDefaultButtonStyle(Texture pressed, Texture normal) {
		NinePatch down = new NinePatch(pressed, 0, pressed.getWidth(), 0,
				pressed.getHeight());
		NinePatch up = new NinePatch(normal, 0, normal.getWidth(), 0,
				normal.getHeight());

		return new Button.ButtonStyle(down, up, null, 0f, 0f, 0f, 0.0f, null,
				Color.BLACK);
	}	
	
}
