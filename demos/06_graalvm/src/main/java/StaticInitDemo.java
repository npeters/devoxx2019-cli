import picocli.CommandLine;

import java.time.LocalDateTime;

import static java.lang.System.out;

public class StaticInitDemo {

  static LocalDateTime COMPILE_TIME = LocalDateTime.now();
  static String PROPERTY_VALUE = System.getProperty("property");

  static {
    printf("static bloc:       %tT%n", COMPILE_TIME);
    printf("static bloc:        %s%n", PROPERTY_VALUE);
  }


  public static void main(String[] args) {
    printf("static variable:   %tT%n", COMPILE_TIME);
    printf("static property:   %s%n", PROPERTY_VALUE);
    printf("runtime time:      %tT%n", LocalDateTime.now());
    printf("runtime property:   %s%n", System.getProperty("property"));
  }












  public static void printf(String format, Object ... args) {
     out.print(CommandLine.Help.Ansi.AUTO.text(
         String.format("@|fg(3) "+
             format.replace(":",":|@"),args)));
  }
}
