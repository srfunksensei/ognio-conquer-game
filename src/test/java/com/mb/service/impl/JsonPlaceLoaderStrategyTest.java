package com.mb.service.impl;

import com.mb.model.GameLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class JsonPlaceLoaderStrategyTest {

    @Autowired
    private JsonPlaceLoaderStrategy underTest;

    @Test
    public void load_defaultFile() {
        final List<GameLocation> result = underTest.load(Optional.empty());
        Assertions.assertNotNull(result, "Expected result");
        Assertions.assertFalse(result.isEmpty(), "Expected result content");
    }

    @Test
    public void load_otherFile() throws URISyntaxException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final Path path = Paths.get(classLoader.getResource("json/test_file.json").toURI());
        final List<GameLocation> result = underTest.load(Optional.of(path.toFile().getAbsolutePath()));
        Assertions.assertNotNull(result, "Expected result");
        Assertions.assertFalse(result.isEmpty(), "Expected result content");
    }

    @Test
    public void load_noContent() throws IOException {
        final Path path = Files.createTempFile(null, ".json");
        final List<GameLocation> result = underTest.load(Optional.of(path.toFile().getAbsolutePath()));
        Assertions.assertNotNull(result, "Expected result");
        Assertions.assertTrue(result.isEmpty(), "Expected no result content");
    }

    @Test
    public void load_emptyFile() throws URISyntaxException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final Path path = Paths.get(classLoader.getResource("json/empty_file.json").toURI());
        final List<GameLocation> result = underTest.load(Optional.of(path.toFile().getAbsolutePath()));
        Assertions.assertNotNull(result, "Expected result");
        Assertions.assertTrue(result.isEmpty(), "Expected no result content");
    }

    @Test
    public void load_unsupportedFileType() throws IOException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            final Path temp = Files.createTempFile(null, ".txt");
            underTest.load(Optional.of(temp.toFile().getAbsolutePath()));
        });
    }
}
