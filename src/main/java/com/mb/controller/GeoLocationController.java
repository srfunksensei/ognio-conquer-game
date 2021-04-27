package com.mb.controller;

import com.mb.exception.LocationAlreadyConqueredException;
import com.mb.exception.LocationNotFoundException;
import com.mb.model.GameLocation;
import com.mb.model.GeoLocation;
import com.mb.service.GeoLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/locations")
public class GeoLocationController {

	@Value("${locations.distance.default}")
	private double distance;

	private final GeoLocationService locationService;

	@Autowired
	public GeoLocationController(final GeoLocationService locationService) {
		this.locationService = locationService;
	}

	@GetMapping(value = "/all")
	public HttpEntity<List<GameLocation>> findAll( //
			@RequestParam("latitude") final double latitude, @RequestParam("longitude") final double longitude, //
			final Pageable pageable) {
		final List<GameLocation> allPlacesWithinRange = getAllPlacesWithinRange(latitude, longitude);

		final List<GameLocation> locations = getPage(pageable, allPlacesWithinRange);
		return new ResponseEntity<>(locations, HttpStatus.OK);
	}

	@GetMapping(value = "/all-open")
	public HttpEntity<List<GameLocation>> findAllOpen( //
			@RequestParam("latitude") final double latitude, @RequestParam("longitude") final double longitude, //
			final Pageable pageable) {
		final List<GameLocation> allPlacesWithinRange = getAllPlacesWithinRange(latitude, longitude);
		final List<GameLocation> allOpenPlacesWithinRange = allPlacesWithinRange.stream().filter(p -> !p.isMarked())
				.collect(Collectors.toList());

		final List<GameLocation> locations = getPage(pageable, allOpenPlacesWithinRange);
		return new ResponseEntity<>(locations, HttpStatus.OK);
	}

	private List<GameLocation> getAllPlacesWithinRange(final double latitude, final double longitude) {
		final GeoLocation fromLocation = GeoLocation.fromDegrees(latitude, longitude);
		return locationService.findPlacesWithinDistance(fromLocation, distance);
	}

	private static List<GameLocation> getPage(final Pageable pageable, final List<GameLocation> places) {
		final PagedListHolder<GameLocation> pagedListHolder = new PagedListHolder<>(places);
		pagedListHolder.setPage(pageable.getPageNumber());
		pagedListHolder.setPageSize(pageable.getPageSize());

		return pagedListHolder.getPageList();
	}
	
	@PutMapping(value = "/{id}")
	public HttpEntity<String> conquer(@PathVariable(name = "id") final long locationId, @RequestBody final Long userId) {
		try {
			locationService.conquerLocation(locationId, userId);
		} catch (LocationNotFoundException e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
		} catch (LocationAlreadyConqueredException e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
