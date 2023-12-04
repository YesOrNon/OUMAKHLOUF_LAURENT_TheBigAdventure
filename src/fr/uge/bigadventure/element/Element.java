package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Objects;

public final class Element implements GameObject {

	private String name;
	private Skin skin;
	private ElementType kind;
	private Point position;
	private int health;
	private ArrayList<Point> zone;
	
	
  public Element(String name, Skin skin, ElementType kind, Point position, int health) {
    this.name = name;
    this.skin = skin;
    this.kind = kind;
    this.position = position;
    this.health = health;
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
	
	public void zone(ArrayList<Point> zone) {
		// int[] vers Arraylist à cause de de setzone
		this.zone = zone;
	}
	
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append("Element : \n");
		builder.append("\t" + name + "\n");
		builder.append("\t" + skin + "\n");
		builder.append("\t" + kind + "\n");
		if (position != null) {
			builder.append("\t" + position.x + ", " + position.y + "\n");
		}
		if (health != 0) {
			builder.append("\t" + health + "\n");
		}
		return builder.toString();
	}

	
}
