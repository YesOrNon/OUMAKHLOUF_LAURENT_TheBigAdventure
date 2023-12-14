package fr.uge.bigadventure.main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import fr.uge.bigadventure.analyser.Lexer;
import fr.uge.bigadventure.analyser.Result;
import fr.uge.bigadventure.element.Graphic;
import fr.umlv.zen5.Application;

public class Main {

	
  
  public static void main(String[] args) throws IOException, Exception {
		//On charge la map
		var path = Path.of("maps/fun.map");
		//on lit le fichier
		var text = Files.readString(path);
		//on n'analyse
		var lexer = new Lexer(text);
		var map = lexer.isMatch(lexer);
		//on affiche en console ce qu'on a recup
		System.out.println(map);
		
//	  BufferedImage image;
//	  try(var input = Main.class.getResourceAsStream("bubble.png")) {
//			image = ImageIO.read(input);
//		}
//		  
//		Application.run(Color.BLACK, context -> {
//		  //Graphic.blackScreen(context);
//		  context.renderFrame(truc -> {
//		  	truc.drawImage(image, 10, 10, null);
//		  });
//      context.pollOrWaitEvent(1000000);
//      context.exit(0);
//	      
//		});
  }

}
