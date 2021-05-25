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
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JsonPlaceLoaderStrategy implements PlaceLoaderStrategy {

	public static final String LOCATIONS_FILE_NAME = "json/data.json";
	
	@Override
	public List<GameLocation> load(final Optional<String> filenameOpt) {
		final List<GameLocation> locations = new ArrayList<>();
		try {
			final Path path = filenameOpt.isPresent() ?
				Paths.get(filenameOpt.get()) :
				getPathWithinJar();

			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(path.toFile());
			locations.addAll(reader.read());
		} catch (IOException | ParseException e) {
			log.error("Could not parse json file");
			e.printStackTrace();
		}

		return locations;
	}

	private Path getPathWithinJar() {
		try {
			final ClassLoader classLoader = getClass().getClassLoader();
			return Paths.get(classLoader.getResource(LOCATIONS_FILE_NAME).toURI());
		} catch (Exception e) {
			final InputStream inputStream = accessFile(LOCATIONS_FILE_NAME);
			final File file = getFile(inputStream);
			return file.toPath();
		}
	}

	private File getFile(final InputStream in) {
		File tempFile;
		try {
			tempFile = File.createTempFile("data", ".json");
			tempFile.deleteOnExit();

			Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.warn("Could not create or copy temp file " + e.getMessage());
			throw new InvalidPathException("", e.getCause().toString());
		}
		return tempFile;
	}

	private InputStream accessFile(final String resource) {
		// this is the path within the jar file
		InputStream input = JsonPlaceLoaderStrategy.class.getResourceAsStream(resource);
		if (input == null) {
			input = JsonPlaceLoaderStrategy.class.getClassLoader().getResourceAsStream(resource);
		}
		return input;
	}
}
