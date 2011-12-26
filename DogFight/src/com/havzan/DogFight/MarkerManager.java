package com.havzan.DogFight;

import java.util.HashMap;
import java.util.Map;

public class MarkerManager {

	private static MarkerManager sInstance = null;
	private Map<IWorldObject, PathMarker> mObjectToMarkerMap = new HashMap<IWorldObject, PathMarker>();

	protected MarkerManager() {
	}

	public static MarkerManager getInstance() {
		if (sInstance == null)
			sInstance = new MarkerManager();
		return sInstance;
	}

	public void registerMarker(IWorldObject obj){
		if (mObjectToMarkerMap.containsKey(obj))
			return;
		PathMarker marker = new PathMarker();
		marker.create();
		mObjectToMarkerMap.put(obj, marker);
	}

	public void updateMarkers() {
		for (Map.Entry<IWorldObject, PathMarker> entry : mObjectToMarkerMap
				.entrySet()) {
			entry.getValue().addLocation(entry.getKey().getLocation());
		}
	}
	
	public void render(){
		for (Map.Entry<IWorldObject, PathMarker> entry : mObjectToMarkerMap
				.entrySet()) {
			entry.getValue().Render();
		}
	}

	public void unregisterMarker(IWorldObject obj) {
		mObjectToMarkerMap.remove(obj);		
	}
}
