package com.havzan.dogfight.game.pilots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.havzan.dogfight.MathUtil;
import com.havzan.dogfight.game.GameWorld;
import com.havzan.dogfight.game.model.Aircraft;

public class RoundCornerPilot {
	private static final String TAG = "RCPilot";
	final GameWorld world;
	private final Aircraft aircraft;
	private State state;

	public float cruiseSecs = 1;
	public boolean turnRight = true;
	public float turnRadians = (float) (Math.PI  / 2);
	public float pullAmount = 1;
	public float thrust = 0.8f;

	private float actualCruiseTime = 0;
	Vector3 preTurnDirection = new Vector3();
	Vector3 currentDirection = new Vector3();
	Vector3 initialRight = new Vector3();
	Vector3 currentRight = new Vector3();

	enum State {
		Turning, Cruising, RollingToTurn, RollingToCruise
	}

	public RoundCornerPilot(GameWorld world, Aircraft aircraft) {
		this.world = world;
		this.aircraft = aircraft;
		state = State.RollingToTurn;
		aircraft.setThrust(thrust);
		aircraft.setStickX(0);
		aircraft.setStickY(0);
		aircraft.getRight(initialRight);
		aircraft.getDirection(preTurnDirection);
	}

	public void update(float delta) {
		switch (state) {
		case Cruising: {
			actualCruiseTime += delta;
			aircraft.setThrust(thrust);
			aircraft.setStickX(0);
			aircraft.setStickY(0);
			if (actualCruiseTime > cruiseSecs) {
				state = State.RollingToTurn;
				aircraft.getRight(initialRight);

			}
			break;
		}
		case RollingToCruise: {
			aircraft.getRight(currentRight);
			float actualRoll = (float) MathUtil.getAngleBetween(currentRight,
					initialRight);
			actualRoll = Math.abs(actualRoll);
			//Gdx.app.log("RCPilot", "Actual Roll : " + actualRoll);

			float remainingRoll = (float) Math.abs(Math.PI / 2 - actualRoll);
			float maxRoll = (float) Math.toRadians(aircraft.params.MaxRollRatePerSec * delta);
			float rollRate = remainingRoll / maxRoll;

			//Gdx.app.log(TAG, "Roll Rate is : " + rollRate);

			if (actualRoll < Math.PI / 2) {
				aircraft.setStickX(turnRight ? -rollRate : rollRate);
				aircraft.setStickY(0);
			} else {
				state = State.Cruising;
				actualCruiseTime = 0;
				aircraft.getDirection(preTurnDirection);
			}
			break;
		}
		case RollingToTurn: {
			aircraft.getRight(currentRight);
			float actualRoll = (float) MathUtil.getAngleBetween(initialRight,
					currentRight);
			actualRoll = Math.abs(actualRoll);
			if (actualRoll < Math.PI / 2) {
				float remainingRoll = (float) Math
						.abs(Math.PI / 2 - actualRoll);
				float maxRoll = (float) Math.toRadians(aircraft.params.MaxRollRatePerSec * delta);
				float rollRate = remainingRoll / maxRoll;

//				Gdx.app.log("RCPilot", "Actual Roll : " + actualRoll
//						+ " Remaining : " + remainingRoll + " Max Roll : " + maxRoll +
//						"rollRate : " + rollRate);
				aircraft.setStickX(turnRight ? rollRate : -rollRate);
				aircraft.setStickY(0);
			} else {
				state = State.Turning;
				aircraft.getDirection(preTurnDirection);
			}

			break;
		}
		case Turning: {
			float actualRoll = (float) MathUtil.getAngleBetween(initialRight,
					currentRight);
//			Gdx.app.log(TAG, "Turning with angle : " + Math.toDegrees(actualRoll));
			aircraft.getDirection(currentDirection);
			float actualTurnRadians = (float) MathUtil.getAngleBetween(
					preTurnDirection, currentDirection);
			actualTurnRadians = Math.abs(actualTurnRadians);
			if (actualTurnRadians < turnRadians) {
				aircraft.setStickY(1);
				aircraft.setStickX(0);
			} else {
				state = State.RollingToCruise;
				aircraft.getRight(initialRight);
			}
			break;
		}
		default:
			break;

		}
	}
}
