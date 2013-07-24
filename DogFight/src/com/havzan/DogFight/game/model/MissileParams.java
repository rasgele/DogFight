package com.havzan.DogFight.game.model;

public class MissileParams {
	public final float MaxSpeedPerSec;
	public final float MaxAccelleration;
	public final float MaxRateOfTurnPreSec;
	public final float MaxTrackingAngle;
	public final float MaxRange;
	
	public MissileParams(float maxSpeedPerSec, float maxAccelleration,
			float maxRateOfTurnPreSec, float maxTrackingAngle, float maxRange) {
		super();
		MaxSpeedPerSec = maxSpeedPerSec;
		MaxAccelleration = maxAccelleration;
		MaxRateOfTurnPreSec = maxRateOfTurnPreSec;
		MaxTrackingAngle = maxTrackingAngle;
		MaxRange = maxRange;
	}
	
	public MissileParams(){
		MaxSpeedPerSec = 900;
		MaxAccelleration = 300;
		MaxRateOfTurnPreSec =90;
		MaxTrackingAngle = (float) (Math.PI / 4);;
		MaxRange = 2000;
	}
}
