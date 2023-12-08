package fr.uge.bigadventure.main;

import java.awt.Color;

import java.awt.geom.Rectangle2D;
import java.nio.file.Files;
import java.nio.file.Path;

import fr.uge.bigadventure.analyser.Lexer;
import fr.uge.bigadventure.analyser.Result;
import fr.uge.bigadventure.element.Graphic;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.ScreenInfo;

import fr.uge.bigadventure.element.Graphic;

public class Main {

	
  
  public static void main(String[] args) throws Exception {
	//On charge la map
	var path = Path.of("maps/fun.map");
	//on lit le fichier
	var text = Files.readString(path);
	//on n'analyse
	var lexer = new Lexer(text);
	Result result;
	var map = lexer.isMatch(lexer);
	//on affiche en console ce qu'on a recup
	System.out.println(map);
	  
	Application.run(Color.BLACK, context -> {
	  Graphic.blackScreen(context);
      
      var event = context.pollOrWaitEvent(1000000);
      context.exit(0);
      
	});
  }

}
