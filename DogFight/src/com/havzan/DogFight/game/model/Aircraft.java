package com.havzan.DogFight.game.model;

import com.badlogic.gdx.math.Vector3;

public class Aircraft extends BaseModelObject {
	private static final String TAG = "Aircraft";
	private float stickX;
	private float stickY;

	public TargetingInfo targetInfo = new TargetingInfo();

	private float thrust;
	public float acceleration;
	public float speed;

	private final String typeId;

	public final AircraftParams params;

	public class TargetingInfo {
		private Aircraft target;
		public float trackingTime;
		public boolean isTracking;
		static final float LockTime = 0; // TODO Get from model??

		void update(float delta) {
			if (target != null && isTracking)
				trackingTime += delta;
		}

		boolean isLocked() {
			return target != null && isTracking ? trackingTime >= LockTime
					: false;
		}

		public Aircraft getTarget() {
			return target;
		}

		public void setTarget(Aircraft target) {
			this.target = target;
			trackingTime = 0f;
			isTracking = true;
		}
	}

	public Aircraft(String name, String typeId, AircraftParams params,
			Vector3 initDirection) {
		super(name, initDirection);
		this.typeId = typeId;
		this.params = params;
	}

	public float getStickX() {
		return stickX;
	}

	public void setStickX(float val) {
		if (val > 1)
			this.stickX = 1;
		else if (val < -1)
			this.stickX = -1;
		else
			this.stickX = val;
	}

	public float getStickY() {
		return stickY;
	}

	public void setStickY(float val) {
		if (val > 1)
			this.stickY = 1;
		else if (val < -1)
			this.stickY = -1;
		else
			this.stickY = val;
	}

	public float getThrust() {
		return thrust;
	}

	public Aircraft setThrust(float val) {
		if (val > 1)
			this.thrust = 1;
		else if (val < -1)
			this.thrust = -1;
		else
			this.thrust = val;
		return this;
	}

	public String getTypeId() {
		return typeId;
	}

}
