package fr.uge.bigadventure.analyser.element;

import java.awt.Point;

public class Player extends Element {
	
	private boolean player;
	
	public Player(String name, Skin skin, Point position, int health) {
		super(name, skin, ElementType.PLAYER, position, health);
		this.player = true;
	}
}
