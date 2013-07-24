package com.havzan.dogfight;

import com.badlogic.gdx.math.Vector3;

public final class MathUtil {
	private MathUtil() {
	}
	
	public static double getAngleBetween(Vector3 vec1, Vector3 vec2) {
		float dot = vec1.dot(vec2);
		return Math.acos(Math.min(1.0, dot));
	}
}
