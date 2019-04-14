import com.oracle.svm.core.annotate.AutomaticFeature;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import org.graalvm.nativeimage.Feature;
import org.graalvm.nativeimage.ImageSingletons;

import java.util.function.BooleanSupplier;


@TargetClass(className = "RuntimeModeProvider", onlyWith = RuntimeModeFeature.IsEnabled.class)
@SuppressWarnings("unused")
final class Target_substititution_RuntimeModeProvider {

  static {
    System.out.println("init Target_substititution_RuntimeModeProvider");
  }

  @SuppressWarnings("unused")
  @Substitute
  public String getMode() {
    return "Native";
  }
}


@AutomaticFeature
final class RuntimeModeFeature implements Feature {

  static {
    System.out.println("init RuntimeModeFeature");
  }

  public boolean isInConfiguration(IsInConfigurationAccess access) {
    boolean classFound = access.findClassByName("RuntimeModeProvider") != null;
    System.out.println("call isInConfiguration: classFound=" + classFound);
    return classFound;
  }

  static final class IsEnabled implements BooleanSupplier {

    public boolean getAsBoolean() {
      System.out.println("call getAsBoolean");
      return ImageSingletons.contains(RuntimeModeFeature.class);
    }
  }
}



