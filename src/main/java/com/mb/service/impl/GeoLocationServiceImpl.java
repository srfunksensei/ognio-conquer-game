package com.mb.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.exception.LocationAlreadyConqueredException;
import com.mb.exception.LocationNotFoundException;
import com.mb.model.GameLocation;
import com.mb.model.GeoLocation;
import com.mb.service.GeoLocationService;
import com.mb.service.PlaceLoaderStrategy;
import com.mb.service.UserService;

@Service
public class GeoLocationServiceImpl implements GeoLocationService {

	private List<GameLocation> locations;
	
	private UserService userService;
	
	@Autowired
	public GeoLocationServiceImpl(final PlaceLoaderStrategy loader, final UserService userService) {
		locations = loader.load();
		this.userService = userService;
	}

	@Override
	public List<GameLocation> findPlacesWithinDistance(GeoLocation location, double distance) {
		return filterPlacesWithinDistance(locations, location, distance);
	}

	private List<GameLocation> filterPlacesWithinDistance(final List<GameLocation> allLocations,
			final GeoLocation location, final double distance) {
		final GeoLocation.BoundingCoordinates bounds = location.boundingCoordinates(distance);

		final Map<Long, GeoLocation> allLocationsMap = allLocations.stream().collect(
				Collectors.toMap(GameLocation::getId, e -> GeoLocation.fromDegrees(e.getDegLat(), e.getDegLon())));
		final Set<Long> locationIds = allLocationsMap.entrySet().stream() //
				.filter(entry -> isLatBetween(bounds.getMin().getRadLat(), bounds.getMax().getRadLat()) //
						.and(isLonBetween(bounds.getMin().getRadLon(), bounds.getMax().getRadLon())) //
						.and(isWithinDistance(location, distance)).test(entry.getValue())) //
				.map(Map.Entry::getKey) //
				.collect(Collectors.toSet());

		return allLocations.stream().filter(e -> locationIds.contains(e.getId())).collect(Collectors.toList());
	}

	private static Predicate<GeoLocation> isLatBetween(double lowerBoundRadians, double upperBoundRadians) {
		return p -> p.getRadLat() >= lowerBoundRadians && p.getRadLat() <= upperBoundRadians;
	}

	private static Predicate<GeoLocation> isLonBetween(double lowerBoundRadians, double upperBoundRadians) {
		return lowerBoundRadians > upperBoundRadians
				? p -> p.getRadLon() >= lowerBoundRadians || p.getRadLon() <= upperBoundRadians
				: p -> p.getRadLon() >= lowerBoundRadians && p.getRadLon() <= upperBoundRadians;
	}

	private static Predicate<GeoLocation> isWithinDistance(GeoLocation location, double distance) {
		return p -> location.distanceTo(p) <= distance;
	}

	@Override
	public Optional<GameLocation> getLocation(final long id) {
		return locations.stream().filter(l -> l.getId() == id).findFirst();
	}

	@Override
	public synchronized void conquerLocation(final long id, final long byUser) throws LocationNotFoundException, LocationAlreadyConqueredException {
		final Optional<GameLocation> locationOpt = getLocation(id);
		if (!locationOpt.isPresent()) {
			throw new LocationNotFoundException("Location: " + id + " not found");
		}
		
		final GameLocation location = locationOpt.get();
		if (location.isMarked()) {
			throw new LocationAlreadyConqueredException("Location: " + id + " already conquered");
		}
		
		location.setMarked(true);
		
		userService.increasePoints(byUser, 1);
	}
}
