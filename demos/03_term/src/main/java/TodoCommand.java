import org.fusesource.jansi.Ansi;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Display;
import picocli.CommandLine;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;


@CommandLine.Command(helpCommand = true)

public class TodoCommand implements Callable<Void> {

  public static void pause() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void pauseShort() {
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public Void call() throws Exception {

    CodeHighlighter.printCode(
        "jline",
        List.of(
            "while(true){ ",
            "  List<String> lines = render(stat);",
            "  Display display = new Display(terminal, false);",
            "  display.updateAnsi(lines, -1);",
            "}"
        )
    );


    final ProcessTodo.TodoList stat = new ProcessTodo.TodoList(
        List.of(
            new ProcessTodo.Todo("Action 0", ProcessTodo.TodoStatus.NOT_START),
            new ProcessTodo.Todo("Action 1", ProcessTodo.TodoStatus.NOT_START),
            new ProcessTodo.Todo("Action 2", ProcessTodo.TodoStatus.NOT_START),
            new ProcessTodo.Todo("Action 3", ProcessTodo.TodoStatus.NOT_START)
        )
    );


    new Thread(() -> {
      for (int i = 0; i < stat.todos.size(); i++) {
        pause();
        stat.todos.get(i).status.set(ProcessTodo.TodoStatus.START);
        pause();
        if (i == 3) {
          stat.todos.get(i).status.set(ProcessTodo.TodoStatus.ERROR);
        } else {
          stat.todos.get(i).status.set(ProcessTodo.TodoStatus.DONE);
        }
      }

    }).start();


    new ProcessTodo(stat).renderLoop();

    Thread.yield();
    return null;

  }


  public static class ProcessTodo {

    private final TodoList stat;

    enum TodoStatus {
      NOT_START(false),
      START(false),
      DONE(true),
      ERROR(true);

      public final boolean complete;

      TodoStatus(boolean complete) {
        this.complete = complete;
      }

    }

    public static class Todo {
      final String label;
      final AtomicReference<TodoStatus> status;

      Todo(String label, TodoStatus status) {
        this.label = label;
        this.status = new AtomicReference<>(status);
      }
    }

    public static class TodoList {
      final List<Todo> todos;

      TodoList(List<Todo> todos) {
        this.todos = todos;
      }
    }

    public ProcessTodo(TodoList stat) {
      this.stat = stat;
    }

    private static String formatTodo(Todo todo) {

      String pattern = null;
      switch (todo.status.get()) {
        case NOT_START: {
          pattern = ansi().fg(Ansi.Color.WHITE).a("  ○ %s \n").reset().toString();
          break;
        }
        case START: {
          pattern = ansi().fg(Ansi.Color.WHITE).a("  ● %s \n").reset().toString();
          break;
        }
        case DONE: {
          pattern = ansi().fgGreen().bold().a("  ✔ %s \n").reset().toString();
          break;
        }
        case ERROR: {
          pattern = ansi().fgBrightRed().bold().a("  ✗︎ %s \n").reset().toString();
          break;
        }
      }

      return new Formatter().format(pattern, todo.label).toString();
    }

    public void renderLoop() throws IOException {
      Display display = buildDisplay();

      boolean pending = true;
      while (pending) {
        List<String> lines = stat.todos
            .stream()
            .map(ProcessTodo::formatTodo)
            .collect(Collectors.toList());
        lines.add(""); // look nicer
        lines.add(""); // look nicer

        Optional<Todo> globalStatus = stat.todos
            .stream()
            .filter(todo -> !todo.status.get().complete)
            .findFirst();

        display.updateAnsi(lines, -1);

        pending = globalStatus.isPresent();
        pauseShort();
      }

    }

    protected Display buildDisplay() throws IOException {
      Terminal terminal = TerminalBuilder.builder()
          .dumb(true)
          .build();

      Display display = new Display(terminal, false);
      Size size = terminal.getSize();
      display.resize(size.getRows(), size.getColumns());
      return display;
    }


  }

}
