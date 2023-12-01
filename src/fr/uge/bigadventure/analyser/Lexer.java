package fr.uge.bigadventure.analyser;

import static java.util.stream.Collectors.joining;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.uge.bigadventure.analyser.element.*; // reste bon chienchien

public class Lexer {
	// On ne souhiate pas affecter VARIABLE_LIST dans la liste des tokens a affecter
	private static final Token[] TOKENS_ARRAY = Token.values();
  private static final List<Token> TOKENS = Arrays.asList(Arrays.copyOf(TOKENS_ARRAY, TOKENS_ARRAY.length - 3));
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
		  new Result(Token.ELEMENT_LIST, "")
		  
		  );
  
  private void checkError(Result result, Token token, String message) throws Exception {
  	if (result.token() != token) {
  		throw new Exception("Error : " + message);
  	}
  }
  
  private boolean validateHeaderAndResult(Result headerResult, Result result) {
    return headerResult.token() == result.token() || headerResult.token() == Token.VARIABLE_LIST;
  }
  
  private HashMap<String, String> processVariableListResult(Lexer lexer, HashMap<String, String> encodingMap, Result result, int error) throws Exception {
		while (!result.content().equals("data")) {
			if (result.token() == Token.IDENTIFIER) {
				if (encodingMap.containsKey(result.content()) == true) {
					System.out.println("Encoding Error : Same enconding for multiple Elements");
					error++;
				}
				if (!Skin.isPresent(result.content())) {
					System.out.println("Encoding Error : " + result + "is not a correct Element");
					error++;
				}
				String key = result.content(); // LAVA
				result = lexer.nextResult();
				checkError(result, Token.LEFT_PARENS, "encoding format");
				result = lexer.nextResult();
				if (result.content().length() != 1) {
					System.out.println("Encoding Error : ELEMENT is represented by a letter");
					error++;
				}
				if (encodingMap.containsValue(result.content())) {
					System.out.println("Encoding Error : Same enconding for multiple Elements");
					error++;
				}
				String value = result.content(); // L
				encodingMap.put(key, value);
				result = lexer.nextResult();
				checkError(result, Token.RIGHT_PARENS, "encoding format");
				result = lexer.nextResult();
			}
			else {
				System.out.println("Error : not an IDENTIFIER");
			}
		}
		return encodingMap;
  }
  
  private char[][] processQuoteResult(Result result, int width, int height, int error) {
  	
		String[] lines = result.content().split("\n");
		
		char[][] charArray = Arrays.stream(lines, 1, lines.length - 1)
		    .map(line -> line.trim().toCharArray())
		    .toArray(char[][]::new);
		
		if (charArray.length != height) {
			System.out.println("Error : Height is not correct in drawing");
			error++;
		}
		
   for (int i = 0; i < charArray.length; i++) {
  	 if (charArray[i].length != width) {
  		 System.out.println("error width");
  	 }
     for (int j = 0; j < charArray[i].length; j++) {
         System.out.print(charArray[i][j] + " ");
     }
     System.out.println();
   }
   return charArray;
   
}
  
  private void setName(Element element, Result result, Lexer lexer) throws Exception {
		result = lexer.nextResult();
		checkError(result, Token.COLON, "name format");
		result = lexer.nextResult();
		checkError(result, Token.IDENTIFIER, "skin format");
  	element.name(result.content());
  }
  
  private void setSkin(Element element, Result result, Lexer lexer) throws Exception {
		result = lexer.nextResult();
		checkError(result, Token.COLON, "skin format");
		result = lexer.nextResult();
		checkError(result, Token.IDENTIFIER, "skin format");
		element.skin(Skin.valueOf(result.content()));
  }
 
  @SuppressWarnings("null")
	private void setPosition(Element element, Result result, Lexer lexer) throws Exception {
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
		checkError(result, Token.LEFT_PARENS, "Number format");
		//Point
		Point position = null;
		position.x = x;
		position.y = y;
  	element.position(position);
  }

  private ElementType setKind(Element element, Result result, Lexer lexer) throws Exception {
		result = lexer.nextResult();
		checkError(result, Token.COLON, "kind format");
		result = lexer.nextResult();
		checkError(result, Token.IDENTIFIER, "kind format");
		element.kind(ElementType.valueOf(result.content()));
		return ElementType.valueOf(result.content());
  }
  
  private void setHealth(Element element, Result result, Lexer lexer) throws Exception {
  	result = lexer.nextResult();
  	checkError(result, Token.COLON, "health format");
  	result = lexer.nextResult();
  	checkError(result, Token.NUMBER, "health format");
		 int hp = Optional.of(result.content())
        .map(Integer::parseInt)
        .orElseThrow(() -> new NumberFormatException("Invalid number format"));
		 element.health(hp);
  }
  
  private void setZone(Element element, Result result, Lexer lexer) throws Exception {
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
  	Point[][] zone = null;
  	for (int x1 = x; x < i; x++) {
  		for (int y2 = y; y < j; y++) {
  			Point p = null;
  			p.x = x;
  			p.y = y;
  			// bon faut pas utiliser add ca marche pas
  			zone[x][y] = p; 
  		}
  	}
  }
  
  private boolean isMatch(Lexer lexer) throws Exception {
  	
  	int error = 0;
  	int width = -1;
  	int height = -1;
  	char[][] charArray = null;
  	Result result, headerResult;
		var encodingMap = new HashMap<String, String>(); // LAVA = L
		
	  for (int index = 0; (index < headerPattern.size()) 
	  		&& ((result = lexer.nextResult()) != null); index++) {
	  	
	  	headerResult = headerPattern.get(index);
	  	
	  	if (headerResult.content().equals("data")) {
	  		index++;
	  		headerResult = headerPattern.get(index);
	  	}
	  	if (!validateHeaderAndResult(headerResult, result)) {
	  		System.out.println("Error : header element missing");
	  		error++;
	  	}
	  	
	  	// SIZE
	  	else if (headerResult.token() == Token.NUMBER) {
	  		// gestion d'erreurs à modifier
	  		int number = Optional.of(result.content())
            .map(Integer::parseInt)
            .orElseThrow(() -> new NumberFormatException("Invalid number format"));
	  		if (width == -1) {
	  			width = number;
	  		}
	  		else if (height == -1) {
	  			height = number;
	  		}
	  	}
	  	
	  	// ENCODING
  		else if (headerResult.token() == Token.VARIABLE_LIST) {
  			encodingMap = processVariableListResult(lexer, encodingMap, result, error);
  		}
	  	
	  	// DATA
	  	else if (headerResult.token() == Token.QUOTE) {
	  		charArray = processQuoteResult(result, width, height, error);
//	  		System.out.println(Arrays.deepToString(charArray));
	  	}
	  	
	  	// ELEMENT
	  	else if (headerResult.token() == Token.ELEMENT_LIST) {
	  		while (!result.content().equals("data")) {
	  			Element element = null;
	  			if (result.token() == Token.IDENTIFIER) {
		  			if (result.content().equals("name")) {
		  				setName(element, result, lexer);
		  			}
		  			else if (result.content().equals("skin")) {
		  				setSkin(element, result, lexer);
	  				}
		  			else if (result.content().equals("position")) {
		  				setPosition(element, result, lexer);
		  			}
		  			else if (result.content().equals("kind")) {
		  				var kind = setKind(element, result, lexer);
		  				if (kind.equals(ElementType.ENEMY)) {
		  					element = (Enemy)element;
		  				}
		  			}
		  			else if (result.content().equals("health")) {
		  				setHealth(element, result, lexer);
		  			}
		  			else if (result.content().equals("player")) {
		  				element = (Player)element;
		  			}
		  			else if (result.content().equals("element")) {
		  				setZone(element, result, lexer);
		  			}
		  		}
	  		}
	  	}
  	}
	  
	  Element[][] elementGrid = null;
	  for (int i = 0; i < width; i++) {
	  	for (int j = 0; j < height; i++) {
	  		encodingMap.getOrDefault(charArray[i][j], text)
	  		;
	  	}
	  }
	  System.out.println("Errors : " + error);
  	return error == 0;
  }
	  
  public static void main(String[] args) throws IOException {
    var path = Path.of("maps/fun.map");
    var text = Files.readString(path);
    var lexer = new Lexer(text);
    Result result;
//    System.out.println(lexer.isMatch(lexer));
    while((result = lexer.nextResult()) != null) {
      System.out.println(result);
    }
  }
  
}