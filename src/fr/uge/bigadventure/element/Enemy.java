package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.ArrayList;

public final class Enemy implements GameObject {

	private EnemyBehavior behavior;
  private String name;
  private Skin skin;
  private ElementType kind;
  private Point position;
  private int health;
  private ArrayList<Point> zone;
	
  public Enemy(String name, Skin skin, Point position, int health, EnemyBehavior behavior) {
    this.name = name;
    this.skin = skin;
    this.kind = ElementType.ENEMY;
    this.position = position;
    this.health = health;
    this.behavior = behavior;
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

	public void behavior(EnemyBehavior behavior) {
		this.behavior = behavior;
	}
}
