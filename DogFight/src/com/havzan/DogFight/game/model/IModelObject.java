package com.havzan.dogfight.game.model;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public interface IModelObject {
	Vector3 getLocation(Vector3 location);
	Vector3 getDirection(Vector3 direction);
	Vector3 getRight(Vector3 right);
	Matrix4 getOrientation();
	String getName();
}
