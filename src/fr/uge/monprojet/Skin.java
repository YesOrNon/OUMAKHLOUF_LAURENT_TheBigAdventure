package fr.uge.monprojet;

public enum Skin {
	BABA, BADBAD, FOFO, IT,
	ALGAE, CLOUD, FLOWER, FOLIAGE, GRASS, LADDER, LILY, PLANK, REED, ROAD,
	SPROUT, TILE, TRACK, VINE, BED, BOG, BOMB, BRICK, CHAIR, CLIFF, DOOR, FENCE,
	GATE, HEDGE, HOUSE, HUSK, HUSKS, WALL;
	
	public static boolean isPresent(String elem) {
		for (var skin : Skin.values()) {
			if (elem.equals(skin.toString())) {
				return true;
			}
		}
		return false;
	}
	
}
