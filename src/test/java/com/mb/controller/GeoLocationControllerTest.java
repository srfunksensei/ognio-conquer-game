package com.mb.controller;

import com.mb.exception.LocationAlreadyConqueredException;
import com.mb.exception.LocationNotFoundException;
import com.mb.model.GameLocation;
import com.mb.model.GeoLocation;
import com.mb.service.GeoLocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeoLocationController.class)
public class GeoLocationControllerTest {

	private static final GeoLocation BELGRADE_LOC = GeoLocation.fromDegrees(44.787197, 20.457273);

	@Autowired
	private MockMvc mvc;

	@MockBean
	private GeoLocationService geoLocationServiceMock;

	@Test
	public void givenLocations_whenGetAllLocations_thenReturnEmptyJsonArray() throws Exception {
		final GeoLocation location = BELGRADE_LOC;
		final List<GameLocation> noLocations = new ArrayList<>();
		
		when(geoLocationServiceMock.findPlacesWithinDistance(eq(location), anyDouble())).thenReturn(noLocations);

		mvc.perform(get("/locations/all") //
				.param("latitude", "" + BELGRADE_LOC.getDegLat()) //
				.param("longitude", "" + BELGRADE_LOC.getDegLon()) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(0)));
		
		verify(geoLocationServiceMock, times(1)).findPlacesWithinDistance(any(GeoLocation.class), anyDouble());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}

	@Test
	public void givenLocations_whenGetAllLocations_thenReturnJsonArray() throws Exception {
		final GameLocation loc5mRadiusFromBg = new GameLocation(1, 44.7871541, 20.4572928, false),
				loc11mRadiusFromBg = new GameLocation(2, 44.7871125, 20.4573169, true);

		final List<GameLocation> locationsNearBelgrade = Collections
				.unmodifiableList(Arrays.asList(loc5mRadiusFromBg, loc11mRadiusFromBg));

		when(geoLocationServiceMock.findPlacesWithinDistance(any(GeoLocation.class), anyDouble())).thenReturn(locationsNearBelgrade);

		mvc.perform(get("/locations/all") //
				.param("latitude", "" + BELGRADE_LOC.getDegLat()) //
				.param("longitude", "" + BELGRADE_LOC.getDegLon()) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(locationsNearBelgrade.size())));
		
		verify(geoLocationServiceMock, times(1)).findPlacesWithinDistance(any(GeoLocation.class), anyDouble());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}
	
	@Test
	public void givenLocations_whenGetAllLocations_thenReturnJsonArrayPaginatedSize() throws Exception {
		final GameLocation loc5mRadiusFromBg = new GameLocation(1, 44.7871541, 20.4572928, false),
				loc11mRadiusFromBg = new GameLocation(2, 44.7871125, 20.4573169, true);

		final List<GameLocation> locationsNearBelgrade = Collections
				.unmodifiableList(Arrays.asList(loc5mRadiusFromBg, loc11mRadiusFromBg));

		when(geoLocationServiceMock.findPlacesWithinDistance(any(GeoLocation.class), anyDouble())).thenReturn(locationsNearBelgrade);

		final int pageSize = 1;
		
		mvc.perform(get("/locations/all") //
				.param("latitude", "" + BELGRADE_LOC.getDegLat()) //
				.param("longitude", "" + BELGRADE_LOC.getDegLon()) //
				.param("size", "" + pageSize) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(pageSize))) //
				.andExpect(jsonPath("$[0].id", equalTo((int) loc5mRadiusFromBg.getId())));
		
		verify(geoLocationServiceMock, times(1)).findPlacesWithinDistance(any(GeoLocation.class), anyDouble());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}
	
	@Test
	public void givenLocations_whenGetAllLocations_thenReturnJsonArrayPaginated_lastPage() throws Exception {
		final GameLocation loc5mRadiusFromBg = new GameLocation(1, 44.7871541, 20.4572928, false),
				loc11mRadiusFromBg = new GameLocation(2, 44.7871125, 20.4573169, true);

		final List<GameLocation> locationsNearBelgrade = Collections
				.unmodifiableList(Arrays.asList(loc5mRadiusFromBg, loc11mRadiusFromBg));

		when(geoLocationServiceMock.findPlacesWithinDistance(any(GeoLocation.class), anyDouble())).thenReturn(locationsNearBelgrade);

		final int pageSize = 1, page = 20;
		
		mvc.perform(get("/locations/all") //
				.param("latitude", "" + BELGRADE_LOC.getDegLat()) //
				.param("longitude", "" + BELGRADE_LOC.getDegLon()) //
				.param("size", "" + pageSize) //
				.param("page", "" + page) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(pageSize))) //
				.andExpect(jsonPath("$[0].id", equalTo((int) loc11mRadiusFromBg.getId())));
		
		verify(geoLocationServiceMock, times(1)).findPlacesWithinDistance(any(GeoLocation.class), anyDouble());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}
	
	@Test
	public void givenLocations_whenGetAllOpenLocations_thenReturnEmptyJsonArray() throws Exception {
		final GeoLocation location = BELGRADE_LOC;
		final List<GameLocation> noLocations = new ArrayList<>();
		
		when(geoLocationServiceMock.findPlacesWithinDistance(eq(location), anyDouble())).thenReturn(noLocations);

		mvc.perform(get("/locations/all-open") //
				.param("latitude", "" + BELGRADE_LOC.getDegLat()) //
				.param("longitude", "" + BELGRADE_LOC.getDegLon()) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(0)));
		
		verify(geoLocationServiceMock, times(1)).findPlacesWithinDistance(any(GeoLocation.class), anyDouble());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}

	@Test
	public void givenLocations_whenGetAllOpenLocations_thenReturnJsonArray() throws Exception {
		final GameLocation loc5mRadiusFromBg = new GameLocation(1, 44.7871541, 20.4572928, false),
				loc11mRadiusFromBg = new GameLocation(2, 44.7871125, 20.4573169, true);

		final List<GameLocation> locationsNearBelgrade = Collections
				.unmodifiableList(Arrays.asList(loc5mRadiusFromBg, loc11mRadiusFromBg));
		final int openLocationCount = (int) locationsNearBelgrade.stream().filter(l -> !l.isMarked()).count();

		when(geoLocationServiceMock.findPlacesWithinDistance(any(GeoLocation.class), anyDouble())).thenReturn(locationsNearBelgrade);

		mvc.perform(get("/locations/all-open") //
				.param("latitude", "" + BELGRADE_LOC.getDegLat()) //
				.param("longitude", "" + BELGRADE_LOC.getDegLon()) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(openLocationCount))) //
				.andExpect(jsonPath("$[0].id", equalTo((int) loc5mRadiusFromBg.getId())));
		
		verify(geoLocationServiceMock, times(1)).findPlacesWithinDistance(any(GeoLocation.class), anyDouble());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}
	
	@Test
	public void givenLocations_whenGetAllOpenLocations_thenReturnJsonArrayPaginatedSize() throws Exception {
		final GameLocation loc5mRadiusFromBg = new GameLocation(1, 44.7871541, 20.4572928, false),
				loc11mRadiusFromBg = new GameLocation(2, 44.7871125, 20.4573169, true);

		final List<GameLocation> locationsNearBelgrade = Collections
				.unmodifiableList(Arrays.asList(loc5mRadiusFromBg, loc11mRadiusFromBg));

		when(geoLocationServiceMock.findPlacesWithinDistance(any(GeoLocation.class), anyDouble())).thenReturn(locationsNearBelgrade);

		final int pageSize = 1;
		
		mvc.perform(get("/locations/all-open") //
				.param("latitude", "" + BELGRADE_LOC.getDegLat()) //
				.param("longitude", "" + BELGRADE_LOC.getDegLon()) //
				.param("size", "" + pageSize) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(pageSize))) //
				.andExpect(jsonPath("$[0].id", equalTo((int) loc5mRadiusFromBg.getId())));
		
		verify(geoLocationServiceMock, times(1)).findPlacesWithinDistance(any(GeoLocation.class), anyDouble());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}
	
	@Test
	public void givenLocations_whenGetAllOpenLocations_thenReturnJsonArrayPaginated_lastPage() throws Exception {
		final GameLocation loc5mRadiusFromBg = new GameLocation(1, 44.7871541, 20.4572928, false),
				loc11mRadiusFromBg = new GameLocation(2, 44.7871125, 20.4573169, true);

		final List<GameLocation> locationsNearBelgrade = Collections
				.unmodifiableList(Arrays.asList(loc5mRadiusFromBg, loc11mRadiusFromBg));

		when(geoLocationServiceMock.findPlacesWithinDistance(any(GeoLocation.class), anyDouble())).thenReturn(locationsNearBelgrade);

		final int pageSize = 1, page = 20;
		
		mvc.perform(get("/locations/all-open") //
				.param("latitude", "" + BELGRADE_LOC.getDegLat()) //
				.param("longitude", "" + BELGRADE_LOC.getDegLon()) //
				.param("size", "" + pageSize) //
				.param("page", "" + page) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(pageSize))) //
				.andExpect(jsonPath("$[0].id", equalTo((int) loc5mRadiusFromBg.getId())));

		verify(geoLocationServiceMock, times(1)).findPlacesWithinDistance(any(GeoLocation.class), anyDouble());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}
	
	@Test
	public void givenLocations_whenConquerLocation_thenReturnNotFound() throws Exception {
		final long id = 1;
		final String exceptionMessage = "Location: " + id + " not found";

		doThrow(new LocationNotFoundException(exceptionMessage)).when(geoLocationServiceMock).conquerLocation(eq(id), anyLong());

		mvc.perform(put("/locations/" + id) //
				.content("" + id) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isNotFound()) //
				.andExpect(content().string(exceptionMessage));

		verify(geoLocationServiceMock, times(1)).conquerLocation(eq(id), anyLong());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}
	
	@Test
	public void givenLocations_whenConquerLocation_thenReturnAlreadyConquered() throws Exception {
		final long id = 1;
		final String exceptionMessage = "Location: " + id + " already conquered";

		doThrow(new LocationAlreadyConqueredException(exceptionMessage)).when(geoLocationServiceMock).conquerLocation(eq(id), anyLong());

		mvc.perform(put("/locations/" + id) //
				.content("" + id) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().is(409)) //
				.andExpect(content().string(exceptionMessage));

		verify(geoLocationServiceMock, times(1)).conquerLocation(eq(id), anyLong());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}
	
	@Test
	public void givenLocations_whenConquerLocation_thenReturnOk() throws Exception {
		doNothing().when(geoLocationServiceMock).conquerLocation(anyLong(), anyLong());

		final long id = 1;
		mvc.perform(put("/locations/" + id) //
				.content("" + id) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk());

		verify(geoLocationServiceMock, times(1)).conquerLocation(anyLong(), anyLong());
		verifyNoMoreInteractions(geoLocationServiceMock);
	}
}
