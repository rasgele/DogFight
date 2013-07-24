package com.havzan.dogfight.game.model;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public abstract class BaseModelObject implements IModelObject {	

	protected Matrix4 orientation = new Matrix4();
	protected Vector3 direction;
	protected Vector3 location = new Vector3();
	private final Vector3 initDirection;
	private final Vector3 initRight = new Vector3(0, 0, 1);
	private final String name;

	BaseModelObject(String name, Vector3 initDirection){
		this.name = name;
		this.initDirection = new Vector3(initDirection);
		direction = new Vector3(initDirection);
	}
	
	public Vector3 getInitDirection(Vector3 direction) {
		return direction.set(initDirection);
	}
	
	@Override
	public Vector3 getLocation(Vector3 location) {
		return location.set(this.location);
	}

	@Override
	public Vector3 getDirection(Vector3 direction) {
		return direction.set(this.direction);
	}	
	@Override
	public Vector3 getRight(Vector3 right){
		Vector3 position = new Vector3();
		orientation.getTranslation(position);
		right.set(initRight);		
		right.mul(orientation);
		right.sub(position).nor();
		return right;
	}
	
	@Override
	public Matrix4 getOrientation(){
		return orientation;
	}
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
	public void setOrientation(Matrix4 orientation){
		this.orientation.set(orientation);
		updateLocation();
		updateDirection();
	}	
		
	private void updateLocation() {
		orientation.getTranslation(location);
	}

	private void updateDirection() {
		Vector3 position = new Vector3();
		orientation.getTranslation(position);
		direction.set(initDirection);		
		direction.mul(orientation);
		direction.sub(position).nor();
	}

	public Vector3 getInitRight(Vector3 right) {
		return right.set(initRight);
	}
}
