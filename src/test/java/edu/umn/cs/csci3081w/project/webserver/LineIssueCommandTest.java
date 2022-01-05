package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LineIssueCommandTest {
  private VisualTransitSimulator sim;

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Test command for line issue.
   */
  @Test
  public void testLineIssue() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    sim = new VisualTransitSimulator(getClass().getClassLoader()
        .getResource("config.txt").getFile(), webServerSessionSpy);
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "registerVehicle");
    commandFromClient.addProperty("id", 10001);
    LineIssueCommand lineIssueCommand = new LineIssueCommand(sim);
    lineIssueCommand.execute(webServerSessionSpy, commandFromClient);
    assertEquals(true, sim.getLines().get(1).isIssueExist());
  }
}
