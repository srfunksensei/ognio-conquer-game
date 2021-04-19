package com.mb.service.util;

import com.mb.model.GameLocation;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

@Slf4j
public class JsonGameLocationFileReader {

	public static final String JSON_EXTENSION = ".json";
	
	public static final String FEATURES_KEY = "features";
	public static final String FEATURE_TYPE = "type";
	public static final String FEATURE_GEOMETRY_KEY = "geometry";
	public static final String FEATURE_GEOMETRY_COORDINATES_KEY = "coordinates";
	public static final String FEATURE_PROPERTIES_KEY = "properties";
	public static final String FEATURE_PROPERTIES_ID_KEY = "id";
	public static final String FEATURE_PROPERTIES_MARKED_KEY = "marked";

	private final Reader reader;
	
	public JsonGameLocationFileReader(final String fileName) throws FileNotFoundException {
		if (fileName == null || !fileName.endsWith(JSON_EXTENSION)) {
			throw new IllegalArgumentException("Unsupported file type");
		}

		this.reader = new FileReader(fileName);
	}
	
	public JsonGameLocationFileReader(final File file) throws FileNotFoundException {
		if (file == null || !file.isFile()) {
			throw new IllegalArgumentException("No file to read from");
		} 
		
		final Optional<String> fileExt = JsonGameLocationFileReader.findExtension(file.getName());
		if (!fileExt.isPresent() || !fileExt.get().endsWith(JSON_EXTENSION)) {
			throw new IllegalArgumentException("Unsupported file type");
		}
		
		this.reader = new FileReader(file);
	}
	
	private static Optional<String> findExtension(final String fileName) {
	    int lastIndex = fileName.lastIndexOf('.');
	    return lastIndex == -1 ? Optional.empty() : Optional.of(fileName.substring(lastIndex));
	}

	public List<GameLocation> read() throws IOException, ParseException {
		final Set<GameLocation> locations = new HashSet<>();

		final JSONParser parser = new JSONParser();
		final JSONObject jsonObject = (JSONObject) parser.parse(reader);
		final JSONObject features = (JSONObject) jsonObject.get(FEATURES_KEY);

		for (@SuppressWarnings("unchecked")
		Iterator<String> key = features.keySet().iterator(); key.hasNext();) {
			JSONObject feature = (JSONObject) features.get(key.next());

			final JSONObject geometry = (JSONObject) feature.get(FEATURE_GEOMETRY_KEY);
			final JSONArray coordinates = (JSONArray) geometry.get(FEATURE_GEOMETRY_COORDINATES_KEY);
			final double[] coords = readFeatureCoordinates(coordinates);

			final JSONObject properties = (JSONObject) feature.get(FEATURE_PROPERTIES_KEY);
			final String id = (String) properties.get(FEATURE_PROPERTIES_ID_KEY);

			final GameLocation loc = new GameLocation(Long.parseLong(id), coords[1], coords[0],
					(boolean) properties.get(FEATURE_PROPERTIES_MARKED_KEY));
			locations.add(loc);
		}

		return new ArrayList<>(locations);
	}

	private double[] readFeatureCoordinates(final JSONArray coordinates) {
		final double[] coords = new double[2];

		int i = 0;
		for (@SuppressWarnings("unchecked")
		Iterator<Double> iterator = coordinates.iterator(); iterator.hasNext();) {
			coords[i++] = iterator.next();
		}

		return coords;
	}
}
