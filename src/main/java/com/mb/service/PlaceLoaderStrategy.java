package com.mb.service;

import java.util.List;

import com.mb.model.GameLocation;

public interface PlaceLoaderStrategy {

	List<GameLocation> load();
}
