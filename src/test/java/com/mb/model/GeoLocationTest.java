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
		assertEquals(71.51841703233163, distance, DELTA);
	}
	
	@Test
	public void testBoundingComponentsWithing50km() {
		final double fiftyKm = 50;
		GeoLocation[] boundingCoordinates = BELGRADE_LOC_DEGREES.boundingCoordinates(fiftyKm);
		
		final GeoLocation nearRabasLoc = GeoLocation.fromDegrees(44.33753690283266, 19.823699946747226),
				nearPlandisteLoc = GeoLocation.fromDegrees(45.23685709716734, 21.09084605325278);
		final GeoLocation[] expectedCoordinates = {nearRabasLoc, nearPlandisteLoc};
		
		assertEquals(expectedCoordinates.length, boundingCoordinates.length);
		for (int i = 0; i < expectedCoordinates.length; i++) {
			assertEquals(expectedCoordinates[i].getDegLat(), boundingCoordinates[i].getDegLat(), DELTA);
			assertEquals(expectedCoordinates[i].getDegLon(), boundingCoordinates[i].getDegLon(), DELTA);
			assertEquals(expectedCoordinates[i].getRadLat(), boundingCoordinates[i].getRadLat(), DELTA);
			assertEquals(expectedCoordinates[i].getRadLon(), boundingCoordinates[i].getRadLon(), DELTA);
		}
	}
}
