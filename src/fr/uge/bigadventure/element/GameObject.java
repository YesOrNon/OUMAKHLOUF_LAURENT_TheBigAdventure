package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.ArrayList;

public sealed interface GameObject permits Element, Player, Enemy  {
	
  void name(String name);
  void skin(Skin skin);
  void position(Point position);
  void health(int health);
	void kind(ElementType kind);
	void zone(ArrayList<Point> zone);
	void damage(int damage);
 
}
