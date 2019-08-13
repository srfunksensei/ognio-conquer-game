package com.mb.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GeoLocationTest {
	
	private static final GeoLocation BELGRADE_LOC_DEGREES = GeoLocation.fromDegrees(44.787197, 20.457273);
	private static final double DELTA = 0.01;

	@Test
	public void testFromRadians() {
		final GeoLocation belgradeLoc = GeoLocation.fromRadians(0.7816840503893268, 0.357046769829338);
		assertEquals(44.787197, belgradeLoc.getDegLat(), DELTA);
		assertEquals(20.457273, belgradeLoc.getDegLon(), DELTA);
	}
	
	@Test
	public void testFromDegrees() {
		assertEquals(0.7816840503893268, BELGRADE_LOC_DEGREES.getRadLat(), DELTA);
		assertEquals(0.357046769829338, BELGRADE_LOC_DEGREES.getRadLon(), DELTA);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFromOutOfRangeDegrees() {
		GeoLocation.fromDegrees(2000, 20.46513);
	}
	
	@Test
	public void testDistanceTo() {
		final GeoLocation noviSadLoc = GeoLocation.fromDegrees(45.267136, 19.833549);
		
		final double distance = BELGRADE_LOC_DEGREES.distanceTo(noviSadLoc);
		assertEquals(72.46195698645441, distance, DELTA);
	}
	
	@Test
	public void testBoundingComponentsWithing50km() {
		final double fiftyKm = 50;
		GeoLocation.BoundingCoordinates boundingCoordinates = BELGRADE_LOC_DEGREES.boundingCoordinates(fiftyKm);
		
		final GeoLocation nearRabasLoc = GeoLocation.fromDegrees(44.33753690283266, 19.823699946747226),
				nearPlandisteLoc = GeoLocation.fromDegrees(45.23685709716734, 21.09084605325278);
		final GeoLocation.BoundingCoordinates expectedCoordinates = new GeoLocation.BoundingCoordinates(nearRabasLoc, nearPlandisteLoc);
		
		assertEquals(expectedCoordinates.getMax().getDegLat(), boundingCoordinates.getMax().getDegLat(), DELTA);
		assertEquals(expectedCoordinates.getMax().getDegLon(), boundingCoordinates.getMax().getDegLon(), DELTA);
		assertEquals(expectedCoordinates.getMin().getDegLat(), boundingCoordinates.getMin().getDegLat(), DELTA);
		assertEquals(expectedCoordinates.getMin().getDegLon(), boundingCoordinates.getMin().getDegLon(), DELTA);

		assertEquals(expectedCoordinates.getMax().getRadLat(), boundingCoordinates.getMax().getRadLat(), DELTA);
		assertEquals(expectedCoordinates.getMax().getRadLon(), boundingCoordinates.getMax().getRadLon(), DELTA);
		assertEquals(expectedCoordinates.getMin().getRadLat(), boundingCoordinates.getMin().getRadLat(), DELTA);
		assertEquals(expectedCoordinates.getMin().getRadLon(), boundingCoordinates.getMin().getRadLon(), DELTA);
	}
}
