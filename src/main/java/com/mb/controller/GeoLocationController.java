package com.mb.controller;

import java.util.List;

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
		final GeoLocation fromLocation = GeoLocation.fromDegrees(latitude, longitude);
		final List<GameLocation> filteredPlaces = locationService.findPlacesWithinDistance(fromLocation, distance);
		
		final PagedListHolder<GameLocation> pagedListHolder = new PagedListHolder<>(filteredPlaces);
		pagedListHolder.setPage(pageable.getPageNumber());
		pagedListHolder.setPageSize(pageable.getPageSize());
		
		return new ResponseEntity<>(pagedListHolder.getSource(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/all-open")
	public ResponseEntity<GameLocation> findAllOpen( //
			@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, //
			Pageable pageable) {
		return null;
	}
	
	@PutMapping(value = "/{id}")
	public void conquer(@PathVariable Long locationId) {
		
	}
}
