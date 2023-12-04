package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.ArrayList;

public final class Player implements GameObject {
	
  private boolean player;
  private String name;
  private Skin skin;
  private Point position;
  private int health;
  private ArrayList<Point> zone;
	
	public Player(String name, Skin skin, Point position, int health) {
    this.name = name;
    this.skin = skin;
    this.position = position;
    this.health = health;
    this.player = true;
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
	
	public void health(int health) {
	    this.health = health;
	}
	
	public void zone(ArrayList<Point> zone) {
	    this.zone = zone;
	}

	public void kind(ElementType kind) {
			return; // probablement besoin de changer
	}

	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append("Player :\n");
		builder.append("\t" + name + "\n");
		builder.append("\t" + skin + "\n");
		if (position != null) {
			builder.append("\t" + position.x + ", " + position.y + "\n");
		}
		builder.append("\t" + health + "\n");
		return builder.toString();
	}
	
}
