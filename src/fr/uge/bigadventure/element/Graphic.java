package fr.uge.bigadventure.element;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.ScreenInfo;

public class Graphic {
  public static float windowWidth;
  public static float windowHeight;
  
	
  public Graphic(ScreenInfo screenInfo) {
	//il doit y avoir une fenetre
	Objects.requireNonNull(screenInfo);
	
	//récup longueur et la largeur de la fenetre
	Graphic.windowWidth = screenInfo.getWidth();
    Graphic.windowHeight = screenInfo.getHeight();
  }
  
  public static float getWidth() {
	  //récupérer la largeur
	  return windowWidth;
  }
  
  public static float getHeight() {
	  //récupérer la longueur
	  return windowHeight;
  }
  
  public static void blackScreen(ApplicationContext context) {
    ScreenInfo screenInfo = context.getScreenInfo();
	//dimension de la fenêtre à titre indicatif
	System.out.println("size of the screen (" + windowWidth +
			  " x " + windowHeight + ")");
	  	
    context.renderFrame(graphics -> {
    //fond noir
    graphics.setColor(Color.BLACK);
    graphics.fill(new  Rectangle2D.Float(0, 0, screenInfo.getWidth(), screenInfo.getHeight()));
    });
  }
  
  public void printThing(Graphics2D map, String thing, int height, int width) {
	  Objects.requireNonNull(map);
	  Objects.requireNonNull(thing);
	  
	  int marginX = (int) ((windowWidth/width) - 10);
	  int marginY = (int) ((windowWidth/height) - 10);
	  
	  //The BufferedImage subclass describes an Image with an accessible buffer of image data.
	  BufferedImage image;
	  
	  // charger des ressources depuis le classpath
	  //var input = getResourceAsStream("img/" + thing.skin() + ".png");
  }
  
  public void graphicMap(Graphics2D map, Map grid) {
	  //on vérifie qu'on a bien une map à afficher
	  Objects.requireNonNull(map);
	  Objects.requireNonNull(grid);
	  //fond noir
	  map.setColor(Color.BLACK);
	  map.fill(new Rectangle2D.Float(0, 0, windowWidth, windowHeight));
	  for(var list : grid.getObstacles()){
	  	for(var element : list){
	  	  //si l'element n'a pas de skin -> on ne fait rien
	  	  //sinon -> on affiche l'élement sur la map
	  		/*
	  	  if (element.skin() != ""){
	  	  	printThing(map, element, grid.getWidth(), grid.getHeight());
	  	  }
	  	  */
	  		
	  	}
	  }
	  
  }
}
