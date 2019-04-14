import me.tongfei.progressbar.ProgressBar;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.CommandLine.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static org.fusesource.jansi.Ansi.ansi;



@Command(
    name = "term-demo",
    version = "Version 0.0.2",
    header =
            "    _____                  _        \n" +
            "  /  __ \\                | |  _    \n" +
            "  | /  \\/ __ _ _ __   __ | |_| |_  \n" +
            "  | |    / _` | '_ \\ / _`| |_   _| \n" +
            "  | \\__/\\ (_| | | | | (_|| | |_|  \n" +
            "  \\____/\\__,_|_| |_|\\__,_|_|     \n",

    footer = "Provided by Canal+",
    description = "@|bold,underline,fg(red) Ansi|@ and @|bold,underline,fg(red) terminfo|@."

)
public class TermDemo implements Callable<Void> {

  @Spec
  Model.CommandSpec spec;

  @Override
  public Void call() throws Exception {
    CommandLine.usage(this.spec, System.out);
    return null;
  }


  public static void main(String[] args) {
    CommandLine cmd = new CommandLine(new TermDemo())
        .addSubcommand("code", new DemoColor())
        .addSubcommand("picoclistyle", new DemoPicocliStyle())
        .addSubcommand("colorpalette", new DemoColorPalette())
        .addSubcommand("back", new DemoBack())
        .addSubcommand("processbar", new ProcessBar())
        .addSubcommand("ansicursor", new AnsiCursor())
        .addSubcommand("todo", new TodoCommand())
        .addSubcommand("iterm", new ITermCommand())
        .addSubcommand("all", new AllCommand())
        .addSubcommand("fire", new DoomFireCommand())
        .addSubcommand("image", new ImageCommand())
        .addSubcommand("help", new HelpCommand());


    cmd.parseWithHandler(new RunFirst(), args);

  }

  @Command(helpCommand = true)
  private static class ImageCommand implements Callable<Void> {

    @Parameters(description = "image to print")
    File file;
    public Void call() throws Exception {

      Terminal terminal = TerminalBuilder.builder().jansi(true).build();
      long startTime = System.currentTimeMillis();
      int screenWidth = 150;

      BufferedImage rawImage = ImageIO.read(file);

      int newHeight = Long.valueOf(  Math.round(screenWidth * 1d  / rawImage.getWidth() * rawImage.getHeight())).intValue();

      BufferedImage image = ImageUtils.resize(rawImage,screenWidth,newHeight);

      ImageUtils.printImage(terminal,image);

      long duration = ( System.currentTimeMillis()-startTime );
      System.out.println("duration: "+duration);
      return null;
    }
  }


  @Command(helpCommand = true)
  private static class DemoPicocliStyle implements Callable<Void> {
    public Void call() throws Exception {
      CodeHighlighter.printCode("picoclistyle", Path.of("TermDemo.java"));

      //begin picoclistyle
      System.out.println("@|STYLE1[,STYLE2]…\u200B text|@");
      System.out.println(Help.Ansi.AUTO.text("@|fg(4) foreground|@").toString());
      System.out.println(Help.Ansi.AUTO.text("@|bg(4) background|@").toString());
      System.out.println(Help.Ansi.AUTO.text("@|bold bold|@").toString());
      System.out.println(Help.Ansi.AUTO.text("@|italic italic|@").toString());
      System.out.println(Help.Ansi.AUTO.text("@|reverse reverse|@").toString());
      System.out.println();
      //end picoclistyle
      return null;
    }
  }



  @Command(helpCommand = true)
  public static class DemoBack implements Callable<Void> {
    public Void call() throws Exception {

      CodeHighlighter.printCode(
          "backspace", Path.of("TermDemo.java")
      );

//begin backspace
      System.out.print("Hello Devoxx");
      for (int i = 0; i < "Devoxx".length(); i++) {
        Thread.sleep(700);
        System.out.print("\b");
      }
      System.out.print("DEVOXX");
//end backspace


      System.out.println();
      System.out.println();
      System.out.println();

      CodeHighlighter.printCode(
          "carriage", Path.of("TermDemo.java")
      );
//begin carriage
      System.out.print("Hello Devoxx");
      Thread.sleep(1000);
      System.out.print("\r");
      Thread.sleep(2000);
      System.out.print("Hello DEVOXX");
//end carriage


      return null;
    }

  }

  @Command(helpCommand = true)
  public static class DemoColor implements Callable<Void> {
    public Void call() throws Exception {


      System.out.print(CodeHighlighter.code("System.out.println(\"\\u001B[38;5;5m Hello Devox\\u001B[0m\");"));
      System.out.print("  // ");
      System.out.println("\u001B[38;5;5m Hello Devoxx\u001B[0m");


      return null;
    }
  }


  @Command(helpCommand = true)
  public static class DemoColorPalette implements Callable<Void> {

    private void showOneColor(int col, String fbg) {
      String pattern = "@|%s(%d) %d |@";
      System.out.print(Help.Ansi.AUTO.text(String.format(pattern, fbg, col, col)).toString());
    }

    private void showIndexedColorPalette() {


      String[] foregroundBackground = {"fg", "bg"};
      for (String fbg : foregroundBackground) {
        for (int r = 0; r < 2; r++) {
          for (int g = 0; g < 6; g++) {
            for (int b = 0; b < 6; b++) {
              int col = 16 + 36 * (0 + 3 * r) + 6 * g + b;
              showOneColor(col, fbg);
            }
            for (int b = 0; b < 6; b++) {
              int col = 16 + 36 * (1 + 3 * r) + 6 * g + b;
              showOneColor(col, fbg);
            }
            for (int b = 0; b < 6; b++) {
              int col = 16 + 36 * (2 + 3 * r) + 6 * g + b;
              showOneColor(col, fbg);
            }
            System.out.println();
          }
          System.out.println();
        }
        int r = 6;
        for (int g = 0; g < 4; g++) {
          for (int b = 0; b < 6; b++) {
            int col = 16 + 36 * r + 6 * g + b;
            showOneColor(col, fbg);
          }
          System.out.println();
        }
        System.out.println();
      }
    }

    public Void call() throws Exception {


      showIndexedColorPalette();

      return null;
    }
  }

  @Command(helpCommand = true)
  public static class Spinner implements Callable<Void> {
    public static class ProcessingStat {
      public boolean isRunning = true;
      public String message = "";

    }
    public Void call() throws Exception {

      CodeHighlighter.printCode(
          "spinner", Path.of("TermDemo.java")
      );

      final AtomicReference<ProcessingStat> processingStat = new AtomicReference<>(new ProcessingStat());

      Thread timer = new Thread(() -> {

        try {
          for (int i = 0; i < 12; i++) {
            Thread.sleep(150);
            processingStat.get().message = String.format("Download content http://www.mycanal/content/%02d", i);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        processingStat.get().isRunning = false;

      });
      timer.start();

      JAnsiUtils.hrAndUp(1);

      //begin spinner
      final List<String> SPINNER_ICONS = List.of("⡿", "⣟", "⣯", "⣷", "⣾", "⣽", "⣻", "⢿");
      for (int i = 0; processingStat.get().isRunning; i++) {
        System.out.print("\r");
        System.out.print(ansi().bold().fgBrightYellow().a(SPINNER_ICONS.get(i)).reset());
        System.out.print(" ");
        System.out.print(processingStat.get().message);
        Thread.sleep(50);
        if (i == SPINNER_ICONS.size() - 1) {
          i = 0;
        }
      }
      //end spinner
      JAnsiUtils.hr(1);
      return null;
    }
  }

  @Command(helpCommand = true)
  public static class ProcessBar implements Callable<Void> {


    public static class ProcessingStat {
      public boolean isRunning = true;
      public String message = "";

    }

    public Void call() throws Exception {


      CodeHighlighter.printCode(
          "progressbar", Path.of("TermDemo.java")
      );

      JAnsiUtils.hrAndUp(2);


      //begin progressbar
      //
      // http://tongfei.me/progressbar/
      // "me.tongfei:progressbar:0.7.2"
      //
      try (ProgressBar pb = new ProgressBar("Download", 100, 50)) { // name, initial max
        for (int i = 0; i <= 100; i++) {
          pb.stepTo(i);
          pb.setExtraMessage("edt " + i);
          Thread.sleep(50);
        }
      }
      //end progressbar
      System.out.println();

      return null;
    }
  }

  // Ansi Cursor command
  @Command(helpCommand = true)
  private static class AnsiCursor implements Callable<Void> {

    public Void call() throws Exception {

      JAnsiUtils.clearScreen(System.out);
      CodeHighlighter.printCode(
          "ansicursor", Path.of("TermDemo.java")
      );
      //begin ansicursor
      System.out.println("");
      System.out.print(ansi().cursorDown(8));
      System.out.println("🦶🦶");
      Thread.sleep(1000);
      System.out.print(ansi().cursorUp(8));
      System.out.println("😆");

      Thread.sleep(2000);

      System.out.print(ansi().cursor(15, 20).a("👽:(15,20)"));
      System.out.print(ansi().cursor(18, 25).a("😸:(18,25)"));
      //end ansicursor
      System.out.print(ansi().cursor(25, 0));
      System.out.println();

      return null;
    }

  }

  /**
   *  ITerm Demo (Mac Os term)
   *  https://www.iterm2.com/documentation-escape-codes.html
   */
  @Command(helpCommand = true)
  private static class ITermCommand implements Callable<Void> {


    final char ESC = 0x1b; // ^[
    final char REST = 0x07; // ^G

    private void run(String cmd) {
      System.out.print(ESC + cmd + REST);
    }

    public Void call() throws Exception {


      clearScrollbackHistory();


      link("www.mycanal.com", "MyCanal");
      // notif growl
      growl("notif Growl Mac");
      System.in.read();

      // fullscreen color
      background("6BB884");
      foreground("C26B49");
      System.in.read();

      background("000000");
      foreground("FFFFFF");

      // imgcat
      file(Optional.empty(), Optional.empty(), false, this.getClass().getResourceAsStream ("/le-bureau-des-legendes-les-sept-questions-pose-fin-saison-spoilers.jpg").readAllBytes());
      return null;
    }


    private void color(String n, String rrggbbColor) {
      run("]P" + n + rrggbbColor);
    }

    private void foreground(String rrggbbColor) {
      color("g", rrggbbColor);
    }

    private void background(String rrggbbColor) {
      color("h", rrggbbColor);
    }

    private void growl(String msg) {
      run("]9;" + msg);
    }

    private void clearScrollbackHistory() {
      run("]1337;ClearScrollback");
    }

    private void link(String url, String text) {
      run("]8;;" + url + REST + text + ESC + "]8;;");
    }


    /**
     *  ITerm wrap File capability
     *  imgcat  https://www.iterm2.com/documentation-images.html
     */
    private void file(Optional<String> width, Optional<String> height, boolean download, byte[] content) {
      final List<String> buff = new ArrayList<>();

      width.ifPresent(s -> buff.add("width=" + s));
      height.ifPresent(s -> buff.add("height=" + s));
      buff.add(download ? "inline=0" : "inline=1");

      var params = String.join(";", buff);
      var b64 = Base64.getEncoder().encodeToString(content);
      run("]1337;File=" + params + " :" + b64);

    }
  }
}