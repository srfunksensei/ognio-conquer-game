package com.mb.service;

import java.util.List;
import java.util.Optional;

import com.mb.model.GameLocation;

public interface PlaceLoaderStrategy {

	List<GameLocation> load(final Optional<String> filename);
}
