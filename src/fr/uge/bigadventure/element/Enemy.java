package fr.uge.bigadventure.element;

import java.awt.Point;

public class Enemy extends Element {

	private EnemyBehavior behavior;
	
	public Enemy(String name, Skin skin, Point position, int health, EnemyBehavior behavior) {
		super(name, skin, ElementType.ENEMY, position, health);
		this.behavior = behavior;
	}
	
	public void behavior(EnemyBehavior behavior) {
		//this.behavior = EnemyBehavior.STROLL;
		this.behavior = behavior;
	}
}
