package com.mb.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class GeoLocationTest {
	
	private static final GeoLocation BELGRADE_LOC_DEGREES = GeoLocation.fromDegrees(44.787197, 20.457273);
	private static final double DELTA = 0.01;

	@Test
	public void testFromRadians() {
		final GeoLocation belgradeLoc = GeoLocation.fromRadians(0.7816840503893268, 0.357046769829338);
		Assertions.assertEquals(44.787197, belgradeLoc.getDegLat(), DELTA);
		Assertions.assertEquals(20.457273, belgradeLoc.getDegLon(), DELTA);
	}
	
	@Test
	public void testFromDegrees() {
		Assertions.assertEquals(0.7816840503893268, BELGRADE_LOC_DEGREES.getRadLat(), DELTA);
		Assertions.assertEquals(0.357046769829338, BELGRADE_LOC_DEGREES.getRadLon(), DELTA);
	}
	
	@Test
	public void testFromOutOfRangeDegrees() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> GeoLocation.fromDegrees(2000, 20.46513));
	}
	
	@Test
	public void testDistanceTo() {
		final GeoLocation noviSadLoc = GeoLocation.fromDegrees(45.267136, 19.833549);
		
		final double distance = BELGRADE_LOC_DEGREES.distanceTo(noviSadLoc);
		Assertions.assertEquals(72.46195698645441, distance, DELTA);
	}
	
	@Test
	public void testBoundingComponentsWithing50km() {
		final double fiftyKm = 50;
		GeoLocation.BoundingCoordinates boundingCoordinates = BELGRADE_LOC_DEGREES.boundingCoordinates(fiftyKm);
		
		final GeoLocation nearRabasLoc = GeoLocation.fromDegrees(44.33753690283266, 19.823699946747226),
				nearPlandisteLoc = GeoLocation.fromDegrees(45.23685709716734, 21.09084605325278);
		final GeoLocation.BoundingCoordinates expectedCoordinates = new GeoLocation.BoundingCoordinates(nearRabasLoc, nearPlandisteLoc);

		Assertions.assertEquals(expectedCoordinates.getMax().getDegLat(), boundingCoordinates.getMax().getDegLat(), DELTA);
		Assertions.assertEquals(expectedCoordinates.getMax().getDegLon(), boundingCoordinates.getMax().getDegLon(), DELTA);
		Assertions.assertEquals(expectedCoordinates.getMin().getDegLat(), boundingCoordinates.getMin().getDegLat(), DELTA);
		Assertions.assertEquals(expectedCoordinates.getMin().getDegLon(), boundingCoordinates.getMin().getDegLon(), DELTA);

		Assertions.assertEquals(expectedCoordinates.getMax().getRadLat(), boundingCoordinates.getMax().getRadLat(), DELTA);
		Assertions.assertEquals(expectedCoordinates.getMax().getRadLon(), boundingCoordinates.getMax().getRadLon(), DELTA);
		Assertions.assertEquals(expectedCoordinates.getMin().getRadLat(), boundingCoordinates.getMin().getRadLat(), DELTA);
		Assertions.assertEquals(expectedCoordinates.getMin().getRadLon(), boundingCoordinates.getMin().getRadLon(), DELTA);
	}
}
