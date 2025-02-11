package junit;

import javafx.application.Platform;
import org.junit.BeforeClass;

public class JavaFXTestBase {

  private static boolean initialized = false;

  @BeforeClass
  public static void initJavaFX() {
    if (!initialized) {
      Platform.startup(() -> {
      });
      initialized = true;
    }
  }
}