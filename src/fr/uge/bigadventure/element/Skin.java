package fr.uge.bigadventure.element;

public enum Skin {
	// éléments décoratifs
	ALGAE, CLOUD, FLOWER, FOLIAGE, GRASS, LADDER, LILY, PLANK, REED, ROAD,
	SPROUT, TILE, TRACK, VINE, BED, 
	// éléments obstacles
	BOG, BOMB, BRICK, CHAIR, CLIFF, DOOR, FENCE, GATE, HEDGE, HOUSE, HUSK, HUSKS, LOCK, MONITOR, 
	PIANO, PILLAR, PIPE, ROCK, RUBBLE, SHELL, SIGN, SPIKE, STATUE, STUMP, TABLE, TOWER, TREE, TREES, WALL,
	// éléments intermittents
	BUBBLE, DUST,
	// éléments amis ou ennemis
	BABA, BADBAD, BAT, BEE, BIRD, BUG, BUNNY, CAT, CRAB, DOG, FISH, FOFO, FROG, GHOST, IT, HELLY, JIJI, KEKE,
	LIAZRD, ME, MONSTER, ROBOT, SNAIL, SKULL, TEETH, TURTLE, WORM,
	// éléments d'inventaire
	BOOK, BOLT, BOX, CASH, CLOCK, COG, CRYSTAL, CUP, DRUM, FLAG, GEM, GUITAR, HIHAT, KEY, LAMP, LEAF, MIRROR,
	MOON, ORB, PANTS, PAPER, PLANET, RING, ROSE, SAX, SCISSORS, SEED, SHIRT, SHOVEL, STAR, STICK, SUN, SWORD,
	TRUMPET, VASE,
	// éléments nutritifs
	BANANA, BOBA, BOTTLE, BURGER, CAKE, CHEESE, DONUT, DRINK, EGG, FRUIT, FUNGUS, FUNGI, LOVE, PIZZA,
	POTATO, PUMPKIN, TURNIP,
	// éléments transport
	PLANE, ROCKET, UFO, CAR, TRAIN, CART,
	// éléments de biome
	ICE, LAVA, WATER,
	// éléments spéciaux
	LEVER, FIRE, WIND
	;
	
	public static boolean isPresent(String elem) {
		for (var skin : Skin.values()) {
			if (elem.equals(skin.toString())) {
				return true;
			}
		}
		return false;
	}
	
}
