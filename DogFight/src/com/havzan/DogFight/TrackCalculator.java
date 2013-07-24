package com.havzan.dogfight;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.math.Vector3;

class TrackCalculator {
	private static final float MAX_TRACK_ANGLE = (float) (Math.PI / 4);
	private static final float MAX_TRACK_DIST_2 = 2000 * 2000;
	
	private static ArrayList<Aircraft> mTrackables = new ArrayList<Aircraft>();

	private TrackCalculator() {
	}

	public static Collection<Aircraft> calculate(Aircraft tracker, Collection<Aircraft> targets) {
		mTrackables.clear();
		final Vector3 trackerDir = tracker.getDirection();
		for (Aircraft target : targets) {
			if (target == tracker)
				continue;

			Vector3 toTarget = target.getLocation().tmp()
					.sub(tracker.getLocation());
			if (MAX_TRACK_DIST_2 < toTarget.len2())
				continue;

			float angleBetween = (float) MathUtil.getAngleBetween(
					trackerDir, toTarget.nor());
			
			if (angleBetween < MAX_TRACK_ANGLE){
				mTrackables.add(target);
			}
		}
		
		return mTrackables;
	}
}