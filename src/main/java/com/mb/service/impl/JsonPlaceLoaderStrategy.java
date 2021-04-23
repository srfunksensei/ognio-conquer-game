package com.mb.service.impl;

import com.mb.model.GameLocation;
import com.mb.service.PlaceLoaderStrategy;
import com.mb.service.util.JsonGameLocationFileReader;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JsonPlaceLoaderStrategy implements PlaceLoaderStrategy {

	public static final String LOCATIONS_FILE_NAME = "json/data.json";
	
	@Override
	public List<GameLocation> load(final Optional<String> filenameOpt) {
		final ClassLoader classLoader = getClass().getClassLoader();

		final List<GameLocation> locations = new ArrayList<>();
		try {
			final Path path = filenameOpt.isPresent() ?
				Paths.get(filenameOpt.get()) :
				Paths.get(classLoader.getResource(LOCATIONS_FILE_NAME).toURI());

			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(path.toFile());
			locations.addAll(reader.read());
		} catch (URISyntaxException e) {
			log.error("Could not read file");
			e.printStackTrace();
		} catch (IOException | ParseException e) {
			log.error("Could not parse json file");
			e.printStackTrace();
		}

		return locations;
	}
}
