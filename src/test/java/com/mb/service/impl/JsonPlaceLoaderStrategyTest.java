package com.mb.service.impl;

import com.mb.model.GameLocation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonPlaceLoaderStrategyTest {

    @Autowired
    private JsonPlaceLoaderStrategy underTest;

    @Test
    public void load_defaultFile() {
        final List<GameLocation> result = underTest.load(Optional.empty());
        Assert.assertNotNull("Expected result", result);
        Assert.assertFalse("Expected result content", result.isEmpty());
    }

    @Test
    public void load_otherFile() throws URISyntaxException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final Path path = Paths.get(classLoader.getResource("json/test_file.json").toURI());
        final List<GameLocation> result = underTest.load(Optional.of(path.toFile().getAbsolutePath()));
        Assert.assertNotNull("Expected result", result);
        Assert.assertFalse("Expected result content", result.isEmpty());
    }

    @Test
    public void load_noContent() throws IOException {
        final Path path = Files.createTempFile(null, ".json");
        final List<GameLocation> result = underTest.load(Optional.of(path.toFile().getAbsolutePath()));
        Assert.assertNotNull("Expected result", result);
        Assert.assertTrue("Expected no result content", result.isEmpty());
    }

    @Test
    public void load_emptyFile() throws URISyntaxException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final Path path = Paths.get(classLoader.getResource("json/empty_file.json").toURI());
        final List<GameLocation> result = underTest.load(Optional.of(path.toFile().getAbsolutePath()));
        Assert.assertNotNull("Expected result", result);
        Assert.assertTrue("Expected no result content", result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void load_unsupportedFileType() throws IOException {
        final Path temp = Files.createTempFile(null, ".txt");
        underTest.load(Optional.of(temp.toFile().getAbsolutePath()));
    }
}
