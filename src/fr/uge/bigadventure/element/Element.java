package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public class Element {
//	Phase 0:
	private String name;
	private Skin skin;
	private ElementType kind;
	private Point position;
	private int health;
	private int[] zone;
	
	
	public Element(String name, Skin skin, ElementType kind, Point position, int health) {
		this.name = name;
		this.skin = Objects.requireNonNull(skin);
		this.kind = kind;
		this.health = health;
		this.position = Objects.requireNonNull(position);
	}
	
	public void name(String name) {
		this.name = name;
	}
	
	public void skin(Skin skin) {
		this.skin = skin;
	}
	
	public void position(Point position) {
		this.position = position;
	}
	
	public void kind(ElementType kind) {
		this.kind = kind;
	}
	
	public void health(int health) {
		this.health = health;
	}
	
	public void zone(int[] zone) {
		this.zone = zone;
	}
}
