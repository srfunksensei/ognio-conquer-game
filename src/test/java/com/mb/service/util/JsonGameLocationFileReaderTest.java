package com.mb.service.util;

import com.mb.model.GameLocation;
import org.assertj.core.util.Files;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonGameLocationFileReaderTest {

	@Test
	public void testInvalidExtensionFileName() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new JsonGameLocationFileReader("test.txt"));
	}

	@Test
	public void testUnknownFileByFileName() {
		Assertions.assertThrows(FileNotFoundException.class, () -> new JsonGameLocationFileReader("test.json"));
	}

	@Test
	public void testInvalidFileExtension() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			final File f = new File("test.txt");
			new JsonGameLocationFileReader(f);
		});
	}

	@Test
	public void testDirectory() {
		final File f = Files.newTemporaryFolder();
		Assertions.assertThrows(IllegalArgumentException.class, () -> new JsonGameLocationFileReader(f));
	}

	@Test
	public void testNoFileExtension() {
		final File f = Files.newTemporaryFile();
		Assertions.assertThrows(IllegalArgumentException.class, () -> new JsonGameLocationFileReader(f));
	}

	private static final String TEST_JSON_FILE_NAME = "test.json";

	private File createNewFileInTempDir(final String file) {
		final File dir = Files.newTemporaryFolder();
		return Files.newFile(dir.getPath() + "/" + file);
	}

	@Test
	public void testEmptyFileRead() {
		final File f = createNewFileInTempDir(JsonGameLocationFileReaderTest.TEST_JSON_FILE_NAME);

		try {
			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(f);
			reader.read();
		} catch (FileNotFoundException e) {
			Assertions.fail("Expected to create new reader");
		} catch (IOException | ParseException e) {
			// expected behavior
		}
	}

	@Test
	public void testBadFormedJson() {
		final File f = createNewFileInTempDir(TEST_JSON_FILE_NAME);

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
			writer.write("{}");
		} catch (Exception e1) {
			Assertions.fail("Expected to write data to file");
		}

		try {
			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(f);
			Assertions.assertThrows(NullPointerException.class, reader::read);
		} catch (IOException e) {
			// expected behavior
		}
	}

	@Test
	public void testEmptyLocationList() {
		final File f = createNewFileInTempDir(TEST_JSON_FILE_NAME);
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
			final String value = "{\n" +
					"\"type\":\"FeatureCollection\",\n" +
					"\"features\":{\n" +
					"}\n" +
					"}";
			writer.write(value);
		} catch (Exception e1) {
			Assertions.fail("Expected to write data to file");
		}

		try {
			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(f);
			final List<GameLocation> locations = reader.read();
			
			Assertions.assertEquals(0, locations.size());
		} catch (IOException | ParseException e) {
			Assertions.fail("Expected to read file");
		}
	}

	@Test
	public void testSuccessfulRead() {
		final GameLocation loc = new GameLocation(108042, 51.5235359, -0.1355294, true);
		
		final File f = createNewFileInTempDir(TEST_JSON_FILE_NAME);
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
			final String value = "{" + "\n" +
					"\"type\":\"FeatureCollection\"," + "\n" +
					"\"" + JsonGameLocationFileReader.FEATURES_KEY + "\":{" + "\n" +
					"\"" + loc.getId() + "\":{" + "\n" +
					"\"" + JsonGameLocationFileReader.FEATURE_TYPE + "\":\"Feature\"," + "\n" +
					"\"" + JsonGameLocationFileReader.FEATURE_GEOMETRY_KEY + "\":{" + "\n" +
					"\"" + JsonGameLocationFileReader.FEATURE_TYPE + "\":\"Point\"," + "\n" +
					"\"" + JsonGameLocationFileReader.FEATURE_GEOMETRY_COORDINATES_KEY + "\":[" + "\n" +
					loc.getDegLon() + "," + "\n" +
					loc.getDegLat() + "\n" +
					"]" + "\n" +
					"}," + "\n" +
					"\"" + JsonGameLocationFileReader.FEATURE_PROPERTIES_KEY + "\":{" + "\n" +
					"\"" + JsonGameLocationFileReader.FEATURE_PROPERTIES_ID_KEY + "\":\"" + loc.getId() + "\"," + "\n" +
					"\"" + JsonGameLocationFileReader.FEATURE_PROPERTIES_MARKED_KEY + "\":" + loc.isMarked() + "" + "\n" +
					"}" + "\n" +
					"}" + "\n" +
					"}" + "\n" +
					"}";
			writer.write(value);
		} catch (Exception e1) {
			Assertions.fail("Expected to write data to file");
		}

		try {
			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(f);
			final List<GameLocation> locations = reader.read();
			
			Assertions.assertEquals(1, locations.size());
			Assertions.assertEquals(loc.getId(), locations.get(0).getId());
			Assertions.assertEquals(loc.getDegLat(), locations.get(0).getDegLat(), 0.01);
			Assertions.assertEquals(loc.getDegLon(), locations.get(0).getDegLon(), 0.01);
			Assertions.assertEquals(loc.isMarked(), locations.get(0).isMarked());
		} catch (IOException | ParseException e) {
			Assertions.fail("Expected to read file");
		}
	}

}
