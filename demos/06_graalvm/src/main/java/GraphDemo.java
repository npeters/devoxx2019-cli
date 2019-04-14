import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.RunFirst;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Callable;


@Command(
    name = "graphdemo",
    version = "Version 0.0.1",
    header =
            "    _____                  _        \n" +
            "  /  __ \\                | |  _    \n" +
            "  | /  \\/ __ _ _ __   __ | |_| |_  \n" +
            "  | |    / _` | '_ \\ / _`| |_   _| \n" +
            "  | \\__/\\ (_| | | | | (_|| | |_|  \n" +
            "  \\____/\\__,_|_| |_|\\__,_|_|     \n",

    footer = "Provided by Canal+",
    description = ""

)
public class GraphDemo implements Callable<Void> {

  public static void main(String[] args) {
    CommandLine cmd = new CommandLine(new GraphDemo());
    cmd.parseWithHandler(new RunFirst(), args);
  }

  @Override
  public Void call() throws Exception {
    new GraphConst().process();
    new GraphParam().process(true);
    new GraphReflect().process();
    new GraphResource().process();

    return null;
  }

  public static class GraphConst {
    private static final boolean CONST =
        "true".equals(System.getProperty("GraphConst"));

    public void process() {
      if (CONST) {
        new GraphConstA().process();
      } else {
        new GraphConstB().process();
      }
    }
  }

  public static class GraphParam {

    public void process(boolean param) {
      if (param) {
        new GraphParamA().process();
      } else {
        new GraphParamB().process();
      }
    }
  }


  public static class GraphReflect {
    public void process() {
      try {
        Class<?> clazz = Class.forName("GraphDemo$GraphReflectA");

        if (clazz.getDeclaredConstructors().length > 0) {
          Process p = (Process) clazz
              .getDeclaredConstructors()[0].newInstance();
          p.process();
        } else {
          System.out.println("Reflection clazz error:");
          System.out.println("DeclaredFields:" +
              Arrays.toString(clazz.getDeclaredFields()));
          System.out.println("DeclaredMethods:" +
              Arrays.toString(clazz.getDeclaredMethods()));

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static class GraphResource {
    public void process() {
      try {
        Properties props = new Properties();
        props.load(this.getClass()
            .getResourceAsStream("/graphdemo.properties"));
        System.out.println(
            "GraphResource: propertyKey:" + props.get("propertyKey") +
                " from graphdemo.properties"
        );
      } catch (Exception e) {
        System.err.println("Resource graphdemo.properties not found");
      }
    }
  }


  public static class GraphConstA {
    public void process() {
      System.out.println(getClass());
    }
  }

  public static class GraphConstB {
    public void process() {
      System.out.println(getClass());
    }
  }

  public static class GraphParamA {
    public void process() {
      System.out.println(getClass());
    }
  }

  public static class GraphParamB {
    public void process() {
      System.out.println(getClass());
    }
  }


  public interface Process {
    void process();
  }

  public static class GraphReflectA implements Process {
    public void process() {
      System.out.println(getClass());
    }
  }

}