package com.havzan.DogFight.game.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.havzan.DogFight.MathUtil;
import com.havzan.DogFight.game.model.Aircraft;
import com.havzan.DogFight.game.model.IModelObject;

public class HUDCalculator {
	private static final float MAX_TRACK_ANGLE = (float) (Math.PI / 4);
	private static final float MAX_TRACK_DIST_2 = 2000 * 2000;
	private static Vector3 trackerPos = new Vector3();
	private static Vector3 trackablePos = new Vector3();
	private static Vector3 trackerDirection = new Vector3();

	private static ArrayList<IModelObject> trackables = new ArrayList<IModelObject>();

	public static void getTrackingData(IModelObject tracker,
			Collection<Aircraft> targets, Camera cam, IModelObject lockedObj,
			List<HUDTrackingData> trackingDataList) {
		trackables.clear();
		calculateTrackables(tracker, targets, trackables);
		calculateTrackingData(tracker, trackables, cam, lockedObj,
				trackingDataList);
	}

	private static Collection<IModelObject> calculateTrackables(
			IModelObject tracker, Collection<Aircraft> targets,
			Collection<IModelObject> trackables) {
		tracker.getDirection(trackerDirection);
		tracker.getLocation(trackerPos);
		for (IModelObject target : targets) {
			if (target == tracker)
				continue;

			Vector3 toTarget = target.getLocation(trackablePos).sub(trackerPos);

			if (MAX_TRACK_DIST_2 < toTarget.len2())
				continue;

			float angleBetween = (float) MathUtil.getAngleBetween(
					trackerDirection, toTarget.nor());

			if (angleBetween < MAX_TRACK_ANGLE) {
				trackables.add(target);
			}
		}

		return trackables;
	}

	private static List<HUDTrackingData> calculateTrackingData(
			IModelObject tracker, Collection<IModelObject> trackables,
			Camera cam, IModelObject lockedObj,
			List<HUDTrackingData> trackingDataList) {

		tracker.getLocation(trackerPos);

		for (IModelObject trackable : trackables) {
			// setTrackingData(cam, lockedObj, trackingDataList, trackable);
		}

		Collections.sort(trackingDataList, new Comparator<HUDTrackingData>() {
			@Override
			public int compare(HUDTrackingData arg0, HUDTrackingData arg1) {
				return arg0.compareTo(arg1);
			}
		});

		return trackingDataList;
	}

	public static void setTrackingData(Camera cam, HUDTrackingData trackingData,
			final Vector3 trackerPosition) {
		Aircraft trackable = trackingData.aircraft;
		trackable.getLocation(trackablePos);
		cam.project(trackablePos);
		

		if (trackablePos.z < 1.0f) {
			trackingData.mPosition.set(trackablePos.x, trackablePos.y);
			trackingData.mDistance2 = trackable.getLocation(trackablePos)
					.sub(trackerPosition).len2();
			trackingData.aircraft = trackable;
		}
	}
}
