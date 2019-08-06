package com.mb.service.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mb.model.GameLocation;

public class JsonGameLocationFileReader {
	
	private Logger logger = Logger.getLogger(JsonGameLocationFileReader.class.getName());

	public static final String FEATURES_KEY = "features";
	public static final String FEATURE_TYPE = "type";
	public static final String FEATURE_GEOMETYRY_KEY = "geometry";
	public static final String FEATURE_GEOMETYRY_COORDINATES_KEY = "coordinates";
	public static final String FEATURE_PROPERTIES_KEY = "properties";
	public static final String FEATURE_PROPERTIES_ID_KEY = "id";
	public static final String FEATURE_PROPERTIES_MARKED_KEY = "marked";

	private Reader reader;
	
	public JsonGameLocationFileReader(final String fileName) throws FileNotFoundException {
		if (fileName == null || !fileName.endsWith(".json")) {
			throw new IllegalArgumentException();
		}

		this.reader = new FileReader(fileName);
	}
	
	public JsonGameLocationFileReader(final File file) throws FileNotFoundException {
		if (file == null || !file.isFile()) {
			throw new IllegalArgumentException();
		} 
		
		final Optional<String> fileExt = JsonGameLocationFileReader.findExtension(file.getName());
		if (!fileExt.isPresent() || !fileExt.get().endsWith("json")) {
			throw new IllegalArgumentException();
		}
		
		this.reader = new FileReader(file);
	}
	
	private static Optional<String> findExtension(final String fileName) {
	    int lastIndex = fileName.lastIndexOf('.');
	    if (lastIndex == -1) {
	        return Optional.empty();
	    }
	    
	    return Optional.of(fileName.substring(lastIndex + 1));
	}

	public List<GameLocation> read() throws IOException, ParseException {
		final Set<GameLocation> locations = new HashSet<>();

		try {
			final JSONParser parser = new JSONParser();
			final JSONObject jsonObject = (JSONObject) parser.parse(reader);
			final JSONObject features = (JSONObject) jsonObject.get(FEATURES_KEY);

			for (@SuppressWarnings("unchecked")
			Iterator<String> key = features.keySet().iterator(); key.hasNext();) {
				JSONObject feature = (JSONObject) features.get(key.next());

				final JSONObject geometry = (JSONObject) feature.get(FEATURE_GEOMETYRY_KEY);
				final JSONArray coordinates = (JSONArray) geometry.get(FEATURE_GEOMETYRY_COORDINATES_KEY);
				final double[] coords = readFeatureCoordinates(coordinates);

				final JSONObject properties = (JSONObject) feature.get(FEATURE_PROPERTIES_KEY);
				final String id = (String) properties.get(FEATURE_PROPERTIES_ID_KEY);

				final GameLocation loc = new GameLocation(Long.parseLong(id), coords[1], coords[0],
						(boolean) properties.get(FEATURE_PROPERTIES_MARKED_KEY));
				locations.add(loc);
			}
		} catch (IOException | ParseException e) {
			logger.log(Level.INFO, "Cannot parse json file");
			throw e;
		}

		return locations.stream().collect(Collectors.toList());
	}

	private double[] readFeatureCoordinates(final JSONArray coordinates) {
		final double[] coords = new double[2];

		int i = 0;
		for (@SuppressWarnings("unchecked")
		Iterator<Double> iterator = coordinates.iterator(); iterator.hasNext();) {
			coords[i++] = (double) iterator.next();
		}

		return coords;
	}
}
