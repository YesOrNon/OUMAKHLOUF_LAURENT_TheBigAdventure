package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.ArrayList;

public final class Element implements GameObject {

	private String name;
	private Skin skin;
	private ElementType kind;
	private Point position;
	private int health;
	private ArrayList<Point> zone;
	private int damage;
	
	
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
	
	public void damage(int damage) {
		this.damage = damage;
	}
	
	public String printZone() {
		var builder = new StringBuilder();
		String corner = "\tTop Left Corner : ";
		builder.append("\tZone : ");
		for (var point : zone) {
			builder.append(corner)
			       .append(point.x).append(", ")
			       .append(point.y);
			corner = "\tTop Right Corner : ";
		}
		builder.append("\n");
		return builder.toString();
	}
	
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append("Element : \n");
		builder.append("\tName : " + name + "\n");
		builder.append("\tSkin : " + skin + "\n");
		builder.append("\tKind : " + kind + "\n");
		builder.append("\tPosition : " + position.x + ", " + position.y + "\n");
		if (health != 0) {builder.append("\tHealth : " + health + "\n");}
		if (damage != 0) {builder.append("\tDamage : " + damage + "\n");}
		if (zone != null) {printZone();}
		return builder.toString();
	}

	
}
