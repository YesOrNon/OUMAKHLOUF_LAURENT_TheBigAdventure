package fr.uge.monprojet;

import java.util.List;

public class Map {
	
	private int height;
	private int width;
	private Element[][] grid;
	private List<Element> elements;
	
	public void setSize(int width, int height) {
		this.height = height;
		this.width = width;
	}
}
