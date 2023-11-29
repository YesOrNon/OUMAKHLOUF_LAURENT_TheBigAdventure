package Map;

import java.awt.Point;

public class Player extends Element {
	
	public Player(String name, Skin skin, Point position, int health) {
		super(name, skin, ElementType.PLAYER, position, health);
	}
}
