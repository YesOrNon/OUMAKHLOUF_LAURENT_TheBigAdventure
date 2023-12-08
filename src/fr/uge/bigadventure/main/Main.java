package fr.uge.bigadventure.main;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.nio.file.Files;
import java.nio.file.Path;

import fr.uge.bigadventure.analyser.Lexer;
import fr.uge.bigadventure.analyser.Result;
import fr.uge.bigadventure.element.Graphic;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ScreenInfo;

public class Main {

	
  
public static void main(String[] args) throws Exception {
	  var path = Path.of("maps/fun.map");
	  var text = Files.readString(path);
	  var lexer = new Lexer(text);
	  Result result;
	  var map = lexer.isMatch(lexer);
	  System.out.println(map);
	  
	  Application.run(Color.BLACK, context -> {
		  System.out.println("ici");
	  	ScreenInfo screenInfo = context.getScreenInfo();
	  	Graphic screen = new Graphic(screenInfo);
	  	float width = screenInfo.getWidth();
	  	float height = screenInfo.getHeight();
	  	System.out.println("size of the screen (" + width + " x " + height + ")");
	  	
      context.renderFrame(graphics -> {
        graphics.setColor(Color.ORANGE);
        graphics.fill(new  Rectangle2D.Float(0, 0, width, height));
      });
      
      var event = context.pollOrWaitEvent(1000000);
      context.exit(0);
      
	  });
	}
}
