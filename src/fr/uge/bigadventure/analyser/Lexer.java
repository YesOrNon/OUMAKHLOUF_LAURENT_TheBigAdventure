package fr.uge.bigadventure.analyser;

import static java.util.stream.Collectors.joining;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.uge.bigadventure.element.Element;
import fr.uge.bigadventure.element.ElementType;
import fr.uge.bigadventure.element.Enemy;
import fr.uge.bigadventure.element.EnemyBehavior;
import fr.uge.bigadventure.element.GameObject;
import fr.uge.bigadventure.element.Map;
import fr.uge.bigadventure.element.Player;
import fr.uge.bigadventure.element.Skin;

public class Lexer {
	private static final Token[] TOKENS_ARRAY = Token.values();
  private static final List<Token> TOKENS = Arrays.asList(Arrays.copyOf(TOKENS_ARRAY, TOKENS_ARRAY.length - 2));
  private static final Pattern PATTERN = Pattern.compile(
      TOKENS.stream()
      	.map(Token::getRegex)
        .<CharSequence>map(regex -> "(" + regex + ")")
        .collect(joining("|")));

  private final String text;
  private final Matcher matcher;

  public Lexer(String text) {
    this.text = Objects.requireNonNull(text);
    this.matcher = PATTERN.matcher(text);
  }

  public Result nextResult() {
    var matches = matcher.find();
    if (!matches) {
      return null;
    }
    for (var group = 1; group <= matcher.groupCount(); group++) {
      var start = matcher.start(group);
      if (start != -1) {
        var end = matcher.end(group);
        var content = text.substring(start, end);
        return new Result(TOKENS.get(group - 1), content);
      }
    }
    throw new AssertionError();
  }
  
  
  // PATTERN
  private List<Result> headerPattern = List.of(
  		
	  // GRID
	  new Result(Token.LEFT_BRACKET, "["),
	  new Result(Token.IDENTIFIER, "grid"),
	  new Result(Token.RIGHT_BRACKET, "]"),
	  
	  // SIZE
	  new Result(Token.IDENTIFIER, "size"),
	  new Result(Token.COLON, ":"),
	  new Result(Token.LEFT_PARENS, "("), 
	  new Result(Token.NUMBER, ""), // number
	  new Result(Token.IDENTIFIER, "x"), 
	  new Result(Token.NUMBER, ""), // number
	  new Result(Token.RIGHT_PARENS, ")"),
	  
	  // ENCODING
	  new Result(Token.IDENTIFIER, "encodings"),
	  new Result(Token.COLON, ":"),
	  new Result(Token.VARIABLE_LIST, ""),
	  
	  // DATA
	  new Result(Token.IDENTIFIER, "data"),
	  new Result(Token.COLON, ":"),
	  new Result(Token.QUOTE, ""),
	  
	  // ELMENENT
	  new Result(Token.LEFT_BRACKET, "["),
	  new Result(Token.ELEMENT_LIST, "")
	  
	  );
  
  // ERROR from token
  private void checkError(Result result, Token token, String message) throws Exception {
  	if (result.token() != token) {
  		System.out.println(result + " " + token);
  		throw new Exception("Error : "  + message);
  	}
  }
  
  private boolean validateHeaderAndResult(Result headerResult, Result result) {
    return headerResult.token() == result.token() || headerResult.token() == Token.VARIABLE_LIST
    		|| headerResult.token() == Token.QUOTE;
  }
  
  
  // ENCODING
  private HashMap<String, String> processVariableListResult(Lexer lexer, HashMap<String, String> encodingMap, Result result) throws Exception {
		while (!result.content().equals("data")) {
			if (result.token() == Token.IDENTIFIER) {
				if (encodingMap.containsValue(result.content())) {
					throw new Exception("Encoding Error : Same enconding for multiple Elements");
				}
				if (!Skin.isPresent(result.content())) {
					throw new Exception("Encoding Error : " + result + "is not a correct Element");
				}
				String value = result.content(); // LAVA
				result = lexer.nextResult();
				checkError(result, Token.LEFT_PARENS, "encoding format");
				result = lexer.nextResult();
				if (result.content().length() != 1) {
					throw new Exception("Encoding Error : ELEMENT is represented by a letter");
				}
				if (encodingMap.containsKey(result.content())) {
					throw new Exception("Encoding Error : Same enconding for multiple Elements");
				}
				String key = result.content(); // L
				encodingMap.put(key, value);
				result = lexer.nextResult();
				checkError(result, Token.RIGHT_PARENS, "encoding format");
			}
			else {
				throw new Exception("Error : not an IDENTIFIER");
			}
			result = lexer.nextResult();
		}
		return encodingMap;
  }
  
  
  // Grid of encodings
  private String[][] processQuoteResult(Result result, int width, int height) throws Exception {
  	String[] lines = result.content().split("\n");
  	String[][] stringArray = Arrays.stream(lines, 1, lines.length - 1)
  	    .map(line -> line.trim().split(""))
  	    .toArray(String[][]::new);
  	if (stringArray.length != height) {
  		throw new Exception("Error : Height is not correct in data");
  	}
  	
  	for (int i = 0; i < stringArray.length; i++) {
  		if (stringArray[i].length != width) {
  		  throw new Exception("Error : Width is not correct in data");
  	  }
  	}
  	return stringArray;
  }
  
  //  ELEMENTS
  private ArrayList<GameObject> processElementList(Lexer lexer, Result result, ArrayList<GameObject> elementsList) throws Exception {
    String name = null;
  	Skin skin = null;
  	Point position = null;
    while (result != null) {
    	// On entre dans un nouvel élément
      if (result.token() == Token.IDENTIFIER && result.content().equals("element")) {
    		GameObject element = new Element(text, null, null, null, 0);
      	while (result != null && result.token() != Token.LEFT_BRACKET) { // toujours dans le même élément
          switch (result.content()) {
          	case "name" :
          		name = setName(element, result, lexer);
          		break;
          	case "skin" :
          		skin = setSkin(element, result, lexer);
          		break;
          	case "position" :
          		position = setPosition(element, result, lexer);
          		break;
          	case "player" : 
          		element = new Player(name, skin, position, 0);
          		break;
          	case "kind" :
          		var kind = setKind(element, result, lexer);
          		if (kind.equals(ElementType.enemy)) {
          			element = new Enemy(name, skin, position, 0, null, 0);
          		} else {
          			element = new Element(name, skin, kind, position, 0);
          		}
          		break;
          	case "health" :
          		setHealth(element, result, lexer);
          		break;
          	case "zone" :
          		setZone(element, result, lexer);
          		break;
          	case "behavior" :
          		setBehavior((Enemy)element, result, lexer);
          		break;
          	case "damage" :
          		setDamage(element, result, lexer);
        		default :
          }
          result = lexer.nextResult();
      	}
        elementsList.add(element);
      }
      result = lexer.nextResult();
    }
		return elementsList;
	}
  
  // NAME
  private String setName(GameObject element, Result result, Lexer lexer) throws Exception {
		result = lexer.nextResult();
		checkError(result, Token.COLON, "name format");
		result = lexer.nextResult();
		checkError(result, Token.IDENTIFIER, "skin format");
  	element.name(result.content());
  	return result.content();
  }
  
  // SKIN
  private Skin setSkin(GameObject element, Result result, Lexer lexer) throws Exception {
		result = lexer.nextResult();
		checkError(result, Token.COLON, "skin format");
		result = lexer.nextResult();
		checkError(result, Token.IDENTIFIER, "skin format");
		element.skin(Skin.valueOf(result.content()));
		return Skin.valueOf(result.content());
  }
 
  // POSITION
	private Point setPosition(GameObject element, Result result, Lexer lexer) throws Exception {
  	int x, y;
		result = lexer.nextResult();
		checkError(result, Token.COLON, "Number format");
		result = lexer.nextResult();
		checkError(result, Token.LEFT_PARENS, "Number format");
		result = lexer.nextResult();
		checkError(result, Token.NUMBER, "Number format");
		x = Optional.of(result.content())
        .map(Integer::parseInt)
        .orElseThrow(() -> new NumberFormatException("Invalid number format"));
		result = lexer.nextResult();
		checkError(result, Token.COMMA, "Number format");
		result = lexer.nextResult();
		checkError(result, Token.NUMBER, "Number format");
		y = Optional.of(result.content())
        .map(Integer::parseInt)
        .orElseThrow(() -> new NumberFormatException("Invalid number format"));
		result = lexer.nextResult();
		checkError(result, Token.RIGHT_PARENS, "Number format");
		//Point
		Point position = new Point(x, y);
  	element.position(position);
  	return position;
  }

  // KIND
  private ElementType setKind(GameObject element, Result result, Lexer lexer) throws Exception {
		result = lexer.nextResult();
		checkError(result, Token.COLON, "kind format");
		result = lexer.nextResult();
		checkError(result, Token.IDENTIFIER, "kind format");
		element.kind(ElementType.valueOf(result.content()));
		return ElementType.valueOf(result.content());
  }
  
  // HEALTH
  private void setHealth(GameObject element, Result result, Lexer lexer) throws Exception {
  	result = lexer.nextResult();
  	checkError(result, Token.COLON, "health format");
  	result = lexer.nextResult();
  	checkError(result, Token.NUMBER, "health format");
		 int hp = Optional.of(result.content())
        .map(Integer::parseInt)
        .orElseThrow(() -> new NumberFormatException("Invalid number format"));
		 element.health(hp);
  }
  
  // ZONE
  private void setZone(GameObject element, Result result, Lexer lexer) throws Exception {
  	result = lexer.nextResult();
  	checkError(result, Token.COLON, "zone format");
  	result = lexer.nextResult();
  	checkError(result, Token.LEFT_PARENS, "zone format");
  	result = lexer.nextResult();
  	checkError(result, Token.NUMBER, "zone format");
  	int x = Optional.of(result.content())
        .map(Integer::parseInt)
        .orElseThrow(() -> new NumberFormatException("Invalid number format"));
  	result = lexer.nextResult();
  	checkError(result, Token.COMMA, "zone format");
  	result = lexer.nextResult();
  	checkError(result, Token.NUMBER, "zone format");
  	int y = Optional.of(result.content())
        .map(Integer::parseInt)
        .orElseThrow(() -> new NumberFormatException("Invalid number format"));
  	result = lexer.nextResult();
  	checkError(result, Token.RIGHT_PARENS, "zone format");
  	result = lexer.nextResult();
  	checkError(result, Token.LEFT_PARENS, "zone format");
  	result = lexer.nextResult();
  	checkError(result, Token.NUMBER, "zone format");
  	int i = Optional.of(result.content())
        .map(Integer::parseInt)
        .orElseThrow(() -> new NumberFormatException("Invalid number format"));
  	result = lexer.nextResult();
  	checkError(result, Token.IDENTIFIER, "zone format");
  	result = lexer.nextResult();
  	checkError(result, Token.NUMBER, "zone format");
  	int j = Optional.of(result.content())
        .map(Integer::parseInt)
        .orElseThrow(() -> new NumberFormatException("Invalid number format"));
  	result = lexer.nextResult();
  	checkError(result, Token.RIGHT_PARENS, "zone format");
  	ArrayList<Point> zone = new ArrayList<Point>();
  	Point upLeft = new Point(x, y);
  	Point downRight = new Point(x + i, j + y);
  	zone.add(upLeft);
  	zone.add(downRight);
  	element.zone(zone);
  }
  
  private void setDamage(GameObject element, Result result, Lexer lexer) throws Exception {
  	result = lexer.nextResult();
  	checkError(result, Token.COLON, "damage format");
  	result = lexer.nextResult();
  	int damage = Optional.of(result.content())
        .map(Integer::parseInt)
        .orElseThrow(() -> new NumberFormatException("Invalid number format"));
  	element.damage(damage);
  }
  
  private void setBehavior(Enemy element, Result result, Lexer lexer) throws Exception {
  	result = lexer.nextResult();
  	checkError(result, Token.COLON, "behavior format");
  	result = lexer.nextResult();
  	checkError(result, Token.IDENTIFIER, "behavior format");
  	element.behavior(EnemyBehavior.valueOf(result.content()));
  }
  
  public Map isMatch(Lexer lexer) throws Exception {
 
  	int width = -1;
  	int height = -1;
  	String[][] stringArray = null;
  	Result result, headerResult;
		var encodingMap = new HashMap<String, String>(); // L = LAVA
		var elementsList = new ArrayList<GameObject>();
		
	  for (int index = 0; (index < headerPattern.size()) 
	  		&& ((result = lexer.nextResult()) != null); index++) {
	  	
	  	headerResult = headerPattern.get(index);
	  	if (headerResult.content().equals("data")) {
	  		index++;
	  		headerResult = headerPattern.get(index);
	  	}
	  	
	  	if (headerResult.token() != Token.ELEMENT_LIST && !validateHeaderAndResult(headerResult, result)) {
	  		throw new Exception("Error : header element missing");
	  	}
	  	
	  	// SIZE
	  	else if (headerResult.token() == Token.NUMBER) {
	  		int number = Optional.of(result.content())
            .map(Integer::parseInt)
            .orElseThrow(() -> new NumberFormatException("Invalid number format"));
	  		if (width == -1) {width = number;}
	  		else if (height == -1) {height = number;}
	  	}
	  	
	  	// ENCODING
  		else if (headerResult.token() == Token.VARIABLE_LIST) {
  			encodingMap = processVariableListResult(lexer, encodingMap, result);
  		}
	  	
	  	// DATA
	  	else if (headerResult.token() == Token.QUOTE) {
	  		stringArray = processQuoteResult(result, width, height);
	  	}
	  	
	  	// ELEMENT
	  	else if (headerResult.token() == Token.ELEMENT_LIST) {
	  		elementsList = processElementList(lexer, result, elementsList);
	  	}
	  }
	  return new Map(width, height, stringArray, elementsList, encodingMap);
  }
  
}