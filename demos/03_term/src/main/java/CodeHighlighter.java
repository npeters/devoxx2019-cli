import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Add Color to the code sample.
 */
public  class CodeHighlighter {

    private static String[] JAVA_KEYWORD = new String[]{
        " class ", " void ", " for ", " public ",  "println","print"," new "," try ",
    };

    public static String code(String line) {
      for (int i = 0; i < JAVA_KEYWORD.length; i++) {
        line = line.replace(JAVA_KEYWORD[i], ansi().fg(Ansi.Color.YELLOW).a(JAVA_KEYWORD[i]).reset().toString());
      }

      return line.replaceAll("(\"[^\"]*\")", ansi().fg(Ansi.Color.YELLOW).a("$1").reset().toString());
    }

    public static void printCode(Path path) {
      try {
        Files.readAllLines(path)
            .forEach(
                line -> System.out.println(CodeHighlighter.code(line))
            );
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public static void line() {
      System.out.println("──────────────────────────────────────────────────");
    }

    public static void title(String title) {
      System.out.println("─────────────────" + title + "───────────────────");
    }

    public static void printCode(String anchor, List<String> lines) {
      title(anchor);
      lines.forEach(line -> System.out.println(CodeHighlighter.code(line)));
      line();
      System.out.println();
    }

  /**
   * Print code between two anchor
   * //begin anchor1
   *   code to print
   * //end anchor2
   * @param anchor
   * @param path
   */
  public static void printCode(String anchor, Path path) {
    try{
      String[] allFileLines = new String(CodeHighlighter.class.getResourceAsStream(path.toString()).readAllBytes(), StandardCharsets.UTF_8).split("\n");
      var lines = Arrays.stream(allFileLines)
            .dropWhile((String l) -> !l.contains("//begin " + anchor))
            .skip(1)
            .takeWhile((String l) -> !l.contains("//end " + anchor))
            .collect(Collectors.toList());


        printCode(anchor, lines);

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }


  }