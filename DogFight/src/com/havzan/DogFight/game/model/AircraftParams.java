package com.havzan.DogFight.game.model;

public class AircraftParams {
	public final float MaxSpeedPerSec;
	public final float MinSpeedPerSec;
	
	public final float MaxRollRatePerSec;
	public final float MaxPitchRatePerSec;
	

	public AircraftParams(float maxSpeedPerSec, float minSpeedPerSec,
			float maxRollRatePerSec, float maxPitchRatePerSec) {
		super();
		MaxSpeedPerSec = maxSpeedPerSec;
		MinSpeedPerSec = minSpeedPerSec;
		MaxRollRatePerSec = maxRollRatePerSec;
		MaxPitchRatePerSec = maxPitchRatePerSec;
	}
	
	public AircraftParams(){
		MaxSpeedPerSec = 600;
		MinSpeedPerSec = 0;
		MaxRollRatePerSec = 180;
		MaxPitchRatePerSec = 20;
	}
}
