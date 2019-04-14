import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Display;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.fusesource.jansi.Ansi.ansi;

@Command(helpCommand = true)
public class AllCommand implements Callable<Void> {
  protected Display buildDisplay(Terminal terminal) throws IOException {

    Display display = new Display(terminal, false);
    Size size = terminal.getSize();
    display.resize(size.getRows(), size.getColumns());
    return display;
  }

  String color(String txt){
    return Help.Ansi.AUTO.text(txt).toString();
  }

  public Void call() throws Exception {
    JAnsiUtils.clearScreen(System.out);
    JAnsiUtils.hr(2);


    Terminal terminal = TerminalBuilder.builder()
        .dumb(true)
        .build();


    Display display = buildDisplay(terminal);

    System.out.println(color("@|fg(3) => ANSI escape code|@"));

    List<String> lines = new ArrayList<>();

    lines.add("                            FG                     REST");
    lines.add("                             |                      | ");
    lines.add("                             v                      v ");
    lines.add(color("System.out.println(\"@|fg(250),bold,italic \\u001B|@@|bold,fg(5) [38;5;5m|@Hello Devox@|bold,italic,fg(250) \\u001B|@@|bold,fg(245) [0m|@\");   // \u001B[38;5;5m Hello Devoxx\u001B[0m"));
    lines.add("                      ^                        ^ ");
    lines.add("                      |                        | ");
    lines.add("                     ESC                      ESC  ");

    display.updateAnsi(lines,-1);



    System.out.println();
    System.out.println();

    display = buildDisplay(terminal);
    lines = new ArrayList<>();

    System.out.println(color("@|fg(3) => Color and Style|@"));

    String println =  color("@|fg(3) println|@");
    lines.add("// @|STYLE1[,STYLE2]…\u200B text|@");
    lines.add("System.out."+println+"(Help.Ansi.AUTO.text("+ CodeHighlighter.code("\"@|fg(4) foreground|@\"")+").toString()); // "+ color("@|fg(4) foreground|@"));
    lines.add("System.out."+println+"(Help.Ansi.AUTO.text("+ CodeHighlighter.code("\"@|bg(4) background|@\"")+").toString()); // "+ color("@|bg(4) background|@"));
    lines.add("System.out."+println+"(Help.Ansi.AUTO.text("+ CodeHighlighter.code("\"@|bold bold|@\"")+").toString());        // "+ color("@|bold bold|@"));
    lines.add("System.out."+println+"(Help.Ansi.AUTO.text("+ CodeHighlighter.code("\"@|italic italic|@\"")+").toString());    // "+ color("@|italic italic|@"));
    lines.add("System.out."+println+"(Help.Ansi.AUTO.text("+ CodeHighlighter.code("\"@|reverse reverse|@\"")+").toString())); // "+ color("@|reverse reverse|@"));

    display.updateAnsi(lines,-1);

    System.in.read();
    JAnsiUtils.hr(2);



    System.out.println(color("@|fg(3) => Backspace|@"));
    display = buildDisplay(terminal);
    String text = "Hello ";
    for (int i = text.length()-1; i >= 0 ; --i) {
      lines = new ArrayList<>();

      StringBuilder backbuf = new StringBuilder();
      for (int j = 0; j <= i ; j++) {
        backbuf.append(j == i ? "@|bg(255) " + text.charAt(j) + "|@" : text.charAt(j));
      }

      lines.add(CodeHighlighter.code("System.out.print(\"Hello\\b\\b\\b\\b\\b\")")+"  //  "+color(backbuf.toString()));
      lines.add("");
      display.updateAnsi(lines, -1);
      Thread.sleep(500);
    }
    System.in.read();

    JAnsiUtils.clearScreen(System.out);
    System.out.println(color("@|fg(3) => Animation|@"));
    new TermDemo.ProcessBar().call();
    System.in.read();

    JAnsiUtils.clearScreen(System.out);

    // format border
    int w = terminal.getWidth();
    int h = terminal.getHeight() - 2;
    String topStr = "┌" + repeat("─",w-2) + ("┐");
    String wallStr = "│" + repeat (" ",w - 2) + "│";
    String bottomStr = "└" +repeat( "─" ,(w - 2)) + "┘";


    System.out.print(topStr);
    for (int i = 0; i <h-2; i++) {
      System.out.print(wallStr);
    }
    System.out.print(bottomStr);

    String sizeText = terminal.getSize().toString();
    String terminfoText = "terminfo and tput";
    System.out.print(ansi().cursor(h/2-1,(w-1)/2-terminfoText.length()/2).a(terminfoText));
    System.out.print(ansi().cursor(h/2,(w-1)/2-sizeText.length()/2).a(sizeText));

    System.out.print(ansi().cursor(terminal.getHeight(),0));
    return null;
  }

  private String repeat(String s, int nb) {
    StringBuilder buf = new StringBuilder();
    for (int j = 0; j < nb; j++) {
      buf.append(s);
    }
    return buf.toString();
  }
}