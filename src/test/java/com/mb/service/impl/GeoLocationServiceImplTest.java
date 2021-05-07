package com.mb.service.impl;

import com.mb.exception.LocationAlreadyConqueredException;
import com.mb.exception.LocationNotFoundException;
import com.mb.model.GameLocation;
import com.mb.model.GeoLocation;
import com.mb.service.PlaceLoaderStrategy;
import com.mb.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeoLocationServiceImplTest {

	private static final GeoLocation BELGRADE_LOC = GeoLocation.fromDegrees(44.787197, 20.457273);

	private static final GameLocation LOC_5M_RADIUS = new GameLocation(1, 44.7871541, 20.4572928, false);
	private static final GameLocation LOC_11M_RADIUS = new GameLocation(2, 44.7871125, 20.4573169, true);
	private static final GameLocation LOC_50M_RADIUS = new GameLocation(3, 44.7873835, 20.4578476, false);

	private static final List<GameLocation> LOCATIONS_NEAR_BELGRADE = Collections
			.unmodifiableList(Arrays.asList(LOC_5M_RADIUS, LOC_11M_RADIUS, LOC_50M_RADIUS));

	@Mock
	private PlaceLoaderStrategy loaderMock;
	
	@Mock
	private UserService userServiceMock;

	private GeoLocationServiceImpl geoLocationService;

	@BeforeEach
	public void setup() {
		when(loaderMock.load(any())).thenReturn(LOCATIONS_NEAR_BELGRADE);

		geoLocationService = new GeoLocationServiceImpl(loaderMock, userServiceMock);
	}

	@Test
	public void findPlacesWithinDistanceNoPlacesFound() {
		final double distanceInKm = 0.001d;

		final List<GameLocation> result = geoLocationService.findPlacesWithinDistance(BELGRADE_LOC, distanceInKm);
		Assertions.assertEquals(0, result.size());
	}

	@Test
	public void findPlacesWithinDistanceFilterPlaces() {
		final double distanceInKm = 0.011d;

		final List<GameLocation> result = geoLocationService.findPlacesWithinDistance(BELGRADE_LOC, distanceInKm);
		Assertions.assertEquals(2, result.size());
	}

	@Test
	public void findPlacesWithinDistanceAllPlaces() {
		final double distanceInKm = 0.1d;

		final List<GameLocation> result = geoLocationService.findPlacesWithinDistance(BELGRADE_LOC, distanceInKm);
		Assertions.assertEquals(3, result.size());
	}

	@Test
	public void getLocation_NoLocationWithId() {
		final Optional<GameLocation> locationOpt = geoLocationService.getLocation(22);
		Assertions.assertFalse(locationOpt.isPresent());
	}

	@Test
	public void getLocation() {
		final Optional<GameLocation> locationOpt = geoLocationService.getLocation(1);
		Assertions.assertTrue(locationOpt.isPresent());
	}
	
	@Test
	public void conquerLocation_NoLocationFoundForConquering() {
		Assertions.assertThrows(LocationNotFoundException.class, () -> geoLocationService.conquerLocation(12345, 12345));
	}
	
	@Test
	public void conquerLocation_LocationAlreadyConquered() {
		final long id = LOC_11M_RADIUS.getId();

		Assertions.assertThrows(LocationAlreadyConqueredException.class, () -> geoLocationService.conquerLocation(id, 12345));
	}
	
	@Test
	public void conquerLocation() {
		boolean isMarked = LOC_5M_RADIUS.isMarked();
		
		final long id = LOC_5M_RADIUS.getId();
		
		try {
			geoLocationService.conquerLocation(id, 12345);
		} catch (LocationNotFoundException | LocationAlreadyConqueredException e) {
			Assertions.fail("Should not throw exception");
		}
		
		Assertions.assertTrue(LOC_5M_RADIUS.isMarked());
		
		// return to original state
		LOC_5M_RADIUS.setMarked(isMarked);
	}
}
