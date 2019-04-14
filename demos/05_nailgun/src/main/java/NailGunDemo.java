import com.facebook.nailgun.NGContext;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;

import java.io.IOException;
import java.util.TreeSet;

public class NailGunDemo {

  public static void nailMain(NGContext context) throws IOException {
    context.out.println();
    context.out.println("         context.getCommand(): " + context.getCommand());
    context.out.println("     context.getInetAddress(): " + context.getInetAddress());
    context.out.println("            context.getPort(): " + context.getPort());
    context.out.println("context.getWorkingDirectory(): " + context.getWorkingDirectory());
    context.out.println("   context.getFileSeparator(): " + context.getFileSeparator());
    context.out.println("   context.getPathSeparator(): " + context.getPathSeparator());

    context.out.println("\ncontext.getArgs():");
    for (int i = 0; i < context.getArgs().length; ++i) {
      context.out.println("   args[" + i + "]=" + context.getArgs()[i]);
    }

    context.out.println("\ncontext.getEnv():");
    TreeSet keys = new TreeSet(context.getEnv().keySet());
    for (Object okey : keys) {
      String key = (String) okey;
      context.out.println("   env[\"" + key + "\"]=" + context.getEnv().getProperty(key));
    }

    // Echo Mode: bidirectional connection
    context.out.println(CommandLine.Help.Ansi.AUTO.text("@|fg(4) Echo Server|@: @|italic Bidirectional socket |@").toString());
    byte[] b = new byte[1024];
    System.out.print("> ");
    int bytesRead = System.in.read(b);
    while (bytesRead != -1) {
      System.out.print("< ");
      System.out.write(b, 0, bytesRead);
      System.out.print("> ");
      bytesRead = System.in.read(b);
    }

  }

}