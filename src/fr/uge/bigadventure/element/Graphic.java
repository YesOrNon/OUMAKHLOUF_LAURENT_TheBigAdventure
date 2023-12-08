package fr.uge.bigadventure.element;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;

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
  
  public void GraphicMap(Graphics2D map) {
	  //on vérifie qu'on a bien une map à afficher
	  Objects.requireNonNull(map);
	  //fond noir
	  map.setColor(Color.BLACK);
	  /*
	  map.fill(new Rectangle2D.Float(0, 0, width de la map, height de la map));
	  for(var : listes de la grille){
	  	for(var : elements de la liste){
	  		si l'element n'a pas de skin -> on ne fait rien
	  		sinon -> on affiche l'élement sur la map
	  	}
	  }
	  */
  }
}
