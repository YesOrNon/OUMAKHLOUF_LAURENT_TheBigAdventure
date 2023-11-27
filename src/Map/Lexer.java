package Map;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

  public static void main(String[] args) throws IOException {
    var path = Path.of("fun.map").toAbsolutePath();
//    var text = Files.readString(path);
//    var lexer = new Lexer(text);
    System.out.println("chemin : " + path);
//    System.out.println(text);
//    System.out.println(lexer);
//    Result result;
//    while((result = lexer.nextResult()) != null) {
//      System.out.println(result);
//      System.out.println("ici\n");
//    }
  }
  
}