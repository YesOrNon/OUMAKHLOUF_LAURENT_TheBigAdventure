package fr.uge.monprojet;

import static java.util.stream.Collectors.joining;

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

public class Lexer {
	// On ne souhiate pas affecter VARIABLE_LIST dans la liste des tokens a affecter
	private static final Token[] TOKENS_ARRAY = Token.values();
  private static final List<Token> TOKENS = Arrays.asList(Arrays.copyOf(TOKENS_ARRAY, TOKENS_ARRAY.length - 1));
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
		  new Result(Token.QUOTE, "")
		  
		  );

  
  private boolean isMatch(Lexer lexer) {
  	int error = 0;
  	int width = -1;
  	int height = -1;
  	Result result, headerResult, tmp;
		var encodingMap = new HashMap<String, String>(); // LAVA = L
	  for (int index = 0; (index < headerPattern.size()) 
	  		&& ((result = lexer.nextResult()) != null); index++) {
	  	headerResult = headerPattern.get(index);
	  	if (headerResult.content().equals("data")) {
	  		index++;
	  		headerResult = headerPattern.get(index);
	  	}
	  	if (headerResult.token() != result.token() 
	  			&& headerResult.token() != Token.VARIABLE_LIST) {
	  		System.out.println("Error : header element missing");
	  		error++;
	  	}
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
  			while (!result.content().equals("data")) {
  				if (result.token() == Token.IDENTIFIER) {
  					if (encodingMap.containsKey(result.content()) == true) {
  						System.out.println("Encoding Error : Same enconding for multiple Elements");
  						System.out.println("\t" + result);
  						error++;
  					}
  					if (!Skin.isPresent(result.content())) {
  						System.out.println("Encoding Error : " + result + "is not a correct Element");
  						error++;
  					}
  					String key = result.content(); // LAVA
  					result = lexer.nextResult();
  					if (result.token() != Token.LEFT_PARENS) {
  						System.out.println("Encoding Error : ELEMENT(E) encoding format is broken");
  						System.out.println("\t ELEMENT is not followed by a (");
  						System.out.println("\t ELEMENT" + result.content());
  						error++;
  					}
  					result = lexer.nextResult();
  					if (result.content().length() != 1) {
  						System.out.println("Encoding Error : ELEMENT is represented by a letter");
  						System.out.println("\tELEMENT(E)");
  						System.out.println("Error on " + result);
  						error++;
  					}
  					if (encodingMap.containsValue(result.content())) {
  						System.out.println("Encoding Error : Same enconding for multiple Elements");
  						System.out.println("\t" + result);
  						System.out.println("\t" + encodingMap.get(key));
  						error++;
  					}
  					String value = result.content(); // L
  					encodingMap.put(key, value);
  					result = lexer.nextResult();
  					if (result.token() != Token.RIGHT_PARENS) {
  						System.out.println("Encoding Error : ELEMENT(E) encoding format is broken");
  						System.out.println("\t ELEMENT(E is not followed by a )");
  						System.out.println("\t" + key + "(" + value + result.content());
  						error++;
  					}
  					result = lexer.nextResult();
  				}
  				else {
  					System.out.println("Error : not an IDENTIFIER");
  				}
  			}
  		}
	  	
	  	// DATA
	  	else if (headerResult.token() == Token.QUOTE) {
	  		String[] lines = result.content().split("\n");
	  		if (lines.length - 1 != width) {
	  			System.out.println("Error : Width is not correct in drawing");
	  			error++;
	  		}
	  		char[][] charArray = Arrays.stream(lines, 1, lines.length - 1)
	  		    .map(line -> line.trim().toCharArray())
	  		    .toArray(char[][]::new);
	  		if (charArray.length != height) {
	  			System.out.println("Error : Height is not correct in drawing");
	  			error++;
	  		}
       for (int i = 0; i < charArray.length; i++) {
         for (int j = 0; j < charArray[i].length; j++) {
             System.out.print(charArray[i][j] + " ");
         }
         System.out.println();
       }
	  		System.out.println(Arrays.deepToString(charArray));
	  	}
  	}	
	  System.out.println("Errors : " + error);
  	return error == 0;
  }
	  
  public static void main(String[] args) throws IOException {
    var path = Path.of("maps/test.map");
    var text = Files.readString(path);
    var lexer = new Lexer(text);
    Result result;
    System.out.println(lexer.isMatch(lexer));
//    while((result = lexer.nextResult()) != null) {
//      System.out.println(result);
//    }
  }
  
}