package com.mb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mb.exception.LocationAlreadyConqueredException;
import com.mb.exception.LocationNotFoundException;
import com.mb.model.GameLocation;
import com.mb.model.GeoLocation;
import com.mb.service.GeoLocationService;

@RestController
@RequestMapping(value = "/locations")
public class GeoLocationController {

	@Value("${locations.distance.default}")
	private double distance;

	private GeoLocationService locationService;

	@Autowired
	public GeoLocationController(final GeoLocationService locationService) {
		this.locationService = locationService;
	}

	@GetMapping(value = "/all")
	public HttpEntity<List<GameLocation>> findAll( //
			@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, //
			Pageable pageable) {
		final List<GameLocation> allPlacesWithinRange = getAllPlacesWithinRange(latitude, longitude);

		final List<GameLocation> locations = getPage(pageable, allPlacesWithinRange);
		return new ResponseEntity<>(locations, HttpStatus.OK);
	}

	@GetMapping(value = "/all-open")
	public HttpEntity<List<GameLocation>> findAllOpen( //
			@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, //
			Pageable pageable) {
		final List<GameLocation> allPlacesWithinRange = getAllPlacesWithinRange(latitude, longitude);
		final List<GameLocation> allOpenPlacesWithinRange = allPlacesWithinRange.stream().filter(p -> !p.isMarked())
				.collect(Collectors.toList());

		final List<GameLocation> locations = getPage(pageable, allOpenPlacesWithinRange);
		return new ResponseEntity<>(locations, HttpStatus.OK);
	}

	private List<GameLocation> getAllPlacesWithinRange(double latitude, double longitude) {
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
	public HttpEntity<String> conquer(@PathVariable(name = "id") long locationId) {
		try {
			locationService.conquerLocation(locationId);
		} catch (LocationNotFoundException e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
		} catch (LocationAlreadyConqueredException e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
