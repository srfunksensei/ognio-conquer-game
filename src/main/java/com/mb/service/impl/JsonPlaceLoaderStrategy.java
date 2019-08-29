package com.mb.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.mb.model.GameLocation;
import com.mb.service.PlaceLoaderStrategy;
import com.mb.service.util.JsonGameLocationFileReader;

@Service
public class JsonPlaceLoaderStrategy implements PlaceLoaderStrategy {
	
	private Logger logger = Logger.getLogger(JsonPlaceLoaderStrategy.class.getName());

	@Autowired
	private ResourceLoader resourceLoader;
	
	private static List<GameLocation> locations;

	@Override
	public List<GameLocation> load() {
		if (locations != null) {
			return locations;
		}
		
		final Resource resource = resourceLoader.getResource("classpath:json/data.json");

		try {
			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(resource.getFile());
			locations = reader.read();
		} catch (IOException | ParseException e) {
			logger.log(Level.INFO, "Could not parse json file");
			e.printStackTrace();
		}
		
		return locations;
	}

}
