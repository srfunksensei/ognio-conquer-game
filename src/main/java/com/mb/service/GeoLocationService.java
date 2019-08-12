package com.mb.service;

import java.util.List;

import com.mb.model.GameLocation;
import com.mb.model.GeoLocation;

public interface GeoLocationService {
	
	List<GameLocation> findPlacesWithinDistance(final GeoLocation location, final double distance);

}
