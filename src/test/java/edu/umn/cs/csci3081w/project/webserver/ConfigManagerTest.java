package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import edu.umn.cs.csci3081w.project.model.Counter;
import edu.umn.cs.csci3081w.project.model.Route;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ConfigManagerTest {

  private ConfigManager configManager;

  /**
   * Tests ReadConfig with valid file name.
   */
  @Test
  public void testReadConfig() {
    configManager = new ConfigManager();
    String fileName = getClass().getClassLoader()
        .getResource("config.txt").getFile();
    Counter counter = spy(Counter.class);
    configManager.readConfig(counter, fileName);
    List<Route> routes = configManager.getRoutes();
    String routeName = routes.get(0).getName();
    assertEquals(routeName, "East Bound");
  }

  /**
   * Tests ReadConfig with invalid file name.
   */
  @Test
  public void testReadConfigBadFileName() {
    configManager = spy(ConfigManager.class);
    String fileName = "badFileName";
    Counter counter = spy(Counter.class);
    configManager.readConfig(counter, fileName);
    verify(configManager).getError(true);
  }
}
