package fr.uge.bigadventure.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Map {
	
	private int height;
	private int width;
	private String[][] obstacles;
	private ArrayList<GameObject> elements;
	private HashMap<String, String> encodingMap;
	
	public Map(int width, int height, String[][] obstacles, ArrayList<GameObject> elements, HashMap<String, String> encodingMap) {
		this.height = height;
		this.width = width;
		this.obstacles = Objects.requireNonNull(obstacles);
		this.elements = Objects.requireNonNull(elements);
		this.encodingMap = Objects.requireNonNull(encodingMap);
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public String[][] getObstacles(){
		return this.obstacles;
	}
	
	public String printObstacles() {
		var builder = new StringBuilder();
		builder.append("Obstacles grid :\n");
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				builder.append(obstacles[i][j]).append("");
			}
			builder.append("\n");
		}
		builder.append("\n");
		return builder.toString();
	}
	
	public String printSize() {
		var builder = new StringBuilder();
		builder.append("Size : ")
						.append("\twidth = ")
						.append(width)
						.append(", height = ")
						.append(height)
						.append("\n\n");
		return builder.toString();
	}
	
	public String printElements() {
		var builder = new StringBuilder();
		builder.append("Elements List :\n");
		for (var element : elements) {
			builder.append(element).append("\n");
		}
		builder.append("\n");
		return builder.toString();
	}
	
	public String printEncodings() {
		var builder = new StringBuilder();
		builder.append("Encodings Map : \n");
		for (var encoding : encodingMap.entrySet()) {
			builder.append("key : ").append(encoding.getKey())
						 .append(", value : ")
						 .append(encoding.getValue())
						 .append("\n");
		}
		builder.append("\n");
		return builder.toString();
	}
	
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append("\t\tMap :\n\n\n")
        		.append(printSize())
        		.append(printEncodings())
        		.append(printObstacles())
        		.append(printElements());
		return builder.toString();
	}
	
}
