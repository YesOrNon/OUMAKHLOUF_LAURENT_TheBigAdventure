package Map;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
  private static final List<Token> TOKENS = List.of(Token.values());
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
		  new Result(Token.IDENTIFIER, " data")
		  
		  );

  
  private boolean isMatch(Lexer lexer) {
  	int error = 0;
  	Result tmp;
		var encodingMap = new HashMap<String, String>(); // LAVA = L
	  for (int index = 0; index < headerPattern.size() && (tmp = lexer.nextResult()) != null;) {
	  	System.out.println("la" + index);
	  	Result headerResult = headerPattern.get(index);
	  	if (headerPattern.get(index) == tmp) {
	  		index++;
	  	}
	  	else if (headerResult.token() == Token.NUMBER && tmp.token() == Token.NUMBER) { // pas bon à revoir
	  		// Problème si .map n'a pas de size
	  		System.out.println(tmp.token() + tmp.content());
	  		int number = Integer.parseInt(tmp.content());
	  		if (number < 0) {
	  			System.out.println("Grid Size Error : " + number + " < 0\n\t" + tmp);
	  			error++;
	  		}
	  		else if (number == 0) {
	  			System.out.println("Grid Size Error : Size of Map = 0\n\t" + tmp);
	  			error++;
	  		}
	  		else if (headerResult.token() == Token.VARIABLE_LIST) {
	  			System.out.println("ici");
	  			while (tmp.token() != Token.IDENTIFIER && !tmp.content().equals("data")) {
	  				if (tmp.token() == Token.IDENTIFIER) {
	  					if (encodingMap.containsKey(tmp.content()) == true) {
	  						System.out.println("Encoding Error : Same enconding for multiple Elements");
	  						System.out.println("\t" + tmp);
	  						System.out.println("\t" + encodingMap.get(tmp.content()));
	  						error++;
	  					}
	  					if (!Skin.isPresent(tmp.content())) {
	  						System.out.println("Encoding Error : " + tmp + "is not a correct Element");
	  						error++;
	  					}
	  					String key = tmp.content(); // LAVA
	  					tmp = lexer.nextResult();
	  					if (tmp.token() != Token.LEFT_PARENS) {
	  						System.out.println("Encoding Error : ELEMENT(E) encoding format is broken");
	  						System.out.println("\t ELEMENT is not followed by a (");
	  						System.out.println("\t ELEMENT" + tmp.content());
	  						error++;
	  					}
	  					tmp = lexer.nextResult();
	  					if (tmp.content().length() != 1) {
	  						System.out.println("Encoding Error : ELEMENT is represented by a letter");
	  						System.out.println("\tELEMENT(E)");
	  						System.out.println("Error on " + tmp);
	  						error++;
	  					}
	  					if (encodingMap.containsValue(tmp.content())) {
	  						System.out.println("Encoding Error : Same enconding for multiple Elements");
	  						System.out.println("\t" + tmp);
	  						System.out.println("\t" + encodingMap.get(key));
	  						error++;
	  					}
	  					String value = tmp.content(); // L
	  					encodingMap.put(key, value);
	  					tmp = lexer.nextResult();
	  					if (tmp.token() != Token.RIGHT_PARENS) {
	  						System.out.println("Encoding Error : ELEMENT(E) encoding format is broken");
	  						System.out.println("\t ELEMENT(E is not followed by a )");
	  						System.out.println("\t" + key + "(" + value + tmp.content());
	  						error++;
	  					}
	  				}
	  				else {
	  					System.out.println("Error : not an IDENTIFIER");
	  				}
	  				tmp = lexer.nextResult();
	  			}
	  		}
	  	}
	  	else if (headerResult.token() == Token.NUMBER && tmp.token() != Token.NUMBER) {
	  		System.out.println("Error : pas de size");
	  		error++;
	  	}
	  	index++;
	  	
	  }
	  System.out.println("Errors : " + error);
  	return error == 0;
  }
	  
  public static void main(String[] args) throws IOException {
    var path = Path.of("maps/fun.map");
    var text = Files.readString(path);
    var lexer = new Lexer(text);
    Result result;
    System.out.println(lexer.isMatch(lexer));
    while((result = lexer.nextResult()) != null) {
      System.out.println(result);
      result.token();
      result.content();
    }
  }
  
}