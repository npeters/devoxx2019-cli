public class HelloWorldRuntimeMode {

  public static void main(String[] args) {
    String mode = new RuntimeModeProvider().getMode();
    System.out.println("Helloworld, I'm a " + mode + " Application");
  }

}

class RuntimeModeProvider {
  String getMode() {
    return "HotSpot";
  }
}