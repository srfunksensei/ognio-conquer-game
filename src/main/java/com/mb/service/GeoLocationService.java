package com.mb.service;

import java.util.List;
import java.util.Optional;

import com.mb.exception.LocationAlreadyConqueredException;
import com.mb.exception.LocationNotFoundException;
import com.mb.model.GameLocation;
import com.mb.model.GeoLocation;

public interface GeoLocationService {
	
	List<GameLocation> findPlacesWithinDistance(final GeoLocation location, final double distance);
	
	Optional<GameLocation> getLocation(final long id);
	void conquerLocation(final long id, final long byUser) throws LocationNotFoundException, LocationAlreadyConqueredException;

}
