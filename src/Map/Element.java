package Map;

import java.awt.Point;
import java.util.Objects;

public class Element {
//	Phase 0:
	private String name;
	private Skin skin;
	private ElementType kind;
	private Point position;
	private int health;
	
//	Suite :
//	private boolean player;
//	private Point zone;
	
	
	public Element(String name, Skin skin, ElementType kind, Point position, int health) {
		this.name = name;
		this.skin = Objects.requireNonNull(skin);
		this.kind = kind;
		this.health = health;
		this.position = Objects.requireNonNull(position);
	}
	
}
