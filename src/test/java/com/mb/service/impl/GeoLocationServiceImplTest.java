package com.mb.service.impl;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mb.model.GameLocation;
import com.mb.model.GeoLocation;
import com.mb.service.PlaceLoaderStrategy;

@RunWith(MockitoJUnitRunner.class)
public class GeoLocationServiceImplTest {

	private static final GeoLocation BELGRADE_LOC = GeoLocation.fromDegrees(44.787197, 20.457273);

	private static final GameLocation LOC_5M_RADIUS = new GameLocation(1, 44.7871541, 20.4572928, false);
	private static final GameLocation LOC_11M_RADIUS = new GameLocation(2, 44.7871125, 20.4573169, true);
	private static final GameLocation LOC_50M_RADIUS = new GameLocation(3, 44.7873835, 20.4578476, false);

	private static final List<GameLocation> LOCATIONS_NEAR_BELGRADE = Collections
			.unmodifiableList(Arrays.asList(LOC_5M_RADIUS, LOC_11M_RADIUS, LOC_50M_RADIUS));

	@Mock
	private PlaceLoaderStrategy loaderMock;

	@InjectMocks
	private GeoLocationServiceImpl geoLocationService;

	@Before
	public void setup() {
		when(loaderMock.load()).thenReturn(LOCATIONS_NEAR_BELGRADE);
	}

	@Test
	public void findPlacesWithinDistanceNoPlacesFound() {
		final double distanceInKm = 0.001d;

		final List<GameLocation> result = geoLocationService.findPlacesWithinDistance(BELGRADE_LOC, distanceInKm);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void findPlacesWithinDistanceFilterPlaces() {
		final double distanceInKm = 0.011d;

		final List<GameLocation> result = geoLocationService.findPlacesWithinDistance(BELGRADE_LOC, distanceInKm);
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void findPlacesWithinDistanceAllPlaces() {
		final double distanceInKm = 0.1d;

		final List<GameLocation> result = geoLocationService.findPlacesWithinDistance(BELGRADE_LOC, distanceInKm);
		Assert.assertEquals(3, result.size());
	}
}
