package com.mb.service.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.assertj.core.util.Files;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import com.mb.model.GameLocation;

public class JsonGameLocationFileReaderTest {

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidExtensionFileName() {
		try {
			new JsonGameLocationFileReader("test.txt");
		} catch (FileNotFoundException e) {
			Assert.fail("expected IllegalArgumentException, but instead reader was created");
		}
	}

	@Test(expected = FileNotFoundException.class)
	public void testUnknownFileByFileName() throws FileNotFoundException {
		new JsonGameLocationFileReader("test.json");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidFileExtension() {
		try {
			final File f = new File("test.txt");
			new JsonGameLocationFileReader(f);
		} catch (FileNotFoundException e) {
			Assert.fail("expected IllegalArgumentException, but instead reader was created");
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDirectory() {
		final File f = Files.newTemporaryFolder();
		try {
			new JsonGameLocationFileReader(f);
		} catch (FileNotFoundException e) {
			Assert.fail("expected IllegalArgumentException, but instead reader was created");
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoFileExtension() {
		final File f = Files.newTemporaryFile();
		try {
			new JsonGameLocationFileReader(f);
		} catch (FileNotFoundException e) {
			Assert.fail("expected IllegalArgumentException, but instead reader was created");
		}
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
			Assert.fail("Expected to create new reader");
		} catch (IOException | ParseException e) {
			// expected behavior
		}
	}

	@Test(expected = NullPointerException.class)
	public void testBadFormedJson() {
		final File f = createNewFileInTempDir(TEST_JSON_FILE_NAME);

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
			writer.write("{}");
		} catch (Exception e1) {
			Assert.fail("Expected to write data to file");
		}

		try {
			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(f);
			reader.read();
		} catch (IOException | ParseException e) {
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
			Assert.fail("Expected to write data to file");
		}

		try {
			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(f);
			final List<GameLocation> locations = reader.read();
			
			Assert.assertEquals(0, locations.size());
		} catch (IOException | ParseException e) {
			Assert.fail("Expected to read file");
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
			Assert.fail("Expected to write data to file");
		}

		try {
			final JsonGameLocationFileReader reader = new JsonGameLocationFileReader(f);
			final List<GameLocation> locations = reader.read();
			
			Assert.assertEquals(1, locations.size());
			Assert.assertEquals(loc.getId(), locations.get(0).getId());
			Assert.assertEquals(loc.getDegLat(), locations.get(0).getDegLat(), 0.01);
			Assert.assertEquals(loc.getDegLon(), locations.get(0).getDegLon(), 0.01);
			Assert.assertEquals(loc.isMarked(), locations.get(0).isMarked());
		} catch (IOException | ParseException e) {
			Assert.fail("Expected to read file");
		}
	}

}
