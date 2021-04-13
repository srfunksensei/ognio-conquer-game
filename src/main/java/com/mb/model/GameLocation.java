package com.mb.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
public final class GameLocation {
	
	@Getter private final long id;
	
	@Getter private final double degLat;  // latitude in degrees
	@Getter private final double degLon;  // longitude in degrees
	
	@Getter @Setter private boolean marked;
}
