package com.havzan.DogFight.game.model;

import com.badlogic.gdx.math.Vector3;
import com.havzan.DogFight.IWorldObject;

public class Missile extends BaseModelObject {
	public static final Vector3 InitDirection = new Vector3(1, 0, 0);
		
	public float acceleration;
	public float speed;
	public final MissileParams params;
	public float distanceTravelled;
	public final IModelObject target;
	public final IModelObject firedBy;
	boolean isTracking;
	
	public Missile(String name, final MissileParams params, IModelObject firedBy, IModelObject target)
	{
		super(name, InitDirection);
		this.params = params;
		this.target = target;
		this.firedBy = firedBy;
		
		isTracking = true;
		
		acceleration = params.MaxAccelleration;
	}

	public IModelObject getTarget() {
		return target;
	}
	
	public boolean getIsTracking(){
		return isTracking;
	}

	public void setIsTracking(boolean b) {
		isTracking = b;		
	}

	public Vector3 getVectorToTarget(Vector3 toTarget) {
		target.getLocation(toTarget);
		return toTarget.sub(this.location);
	}
}
