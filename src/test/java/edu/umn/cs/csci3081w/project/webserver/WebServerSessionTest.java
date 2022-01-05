package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class WebServerSessionTest {
  private WebServerSession webServerSessionSpy;
  private VisualTransitSimulator simulator;
  private List<Integer> vehicleStartTimings;
  private int numTimeSteps;

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;

    vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0, 1);
    vehicleStartTimings.add(1, 1);
    numTimeSteps = 50;

    webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    simulator = new VisualTransitSimulator(getClass().getClassLoader()
        .getResource("config.txt").getFile(), webServerSessionSpy);
    simulator.setVehicleFactories(5);
    simulator.start(vehicleStartTimings, numTimeSteps);
  }

  /**
   * Test command for initializing the simulation.
   */
  @Test
  public void testSimulationInitialization() {
    Session sessionDummy = mock(Session.class);
    webServerSessionSpy.onOpen(sessionDummy);
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "initLines");
    webServerSessionSpy.onMessage(commandFromClient.toString());
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(webServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject commandToClient = messageCaptor.getValue();
    assertEquals("2", commandToClient.get("numLines").getAsString());
  }

  /**
   * Test command for getting vehicles.
   */
  @Test
  public void testGetVehiclesCommand() {
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    webServerSessionSpy.setSimulator(simulator);
    Session sessionDummy = mock(Session.class);
    webServerSessionSpy.onOpen(sessionDummy);
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "getVehicles");
    webServerSessionSpy.onMessage(commandFromClient.toString());
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(webServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject commandToClient = messageCaptor.getValue();
    assertEquals("SMALL_BUS_VEHICLE", commandToClient.get("vehicles").getAsJsonArray()
        .get(0).getAsJsonObject().get("type").getAsString());
    assertEquals("ELECTRIC_TRAIN_VEHICLE", commandToClient.get("vehicles").getAsJsonArray()
        .get(1).getAsJsonObject().get("type").getAsString());
    assertEquals("1001", commandToClient.get("vehicles").getAsJsonArray()
        .get(2).getAsJsonObject().get("id").getAsString());
    assertEquals("DIESEL_TRAIN_VEHICLE", commandToClient.get("vehicles").getAsJsonArray()
        .get(3).getAsJsonObject().get("type").getAsString());
  }

  /**
   * Test command for getting routes.
   */
  @Test
  public void testGetRoutes() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    webServerSessionSpy.onOpen(sessionDummy);
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "getRoutes");
    webServerSessionSpy.onMessage(commandFromClient.toString());
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(webServerSessionSpy).sendJson((messageCaptor.capture()));
    JsonObject commandToClient = messageCaptor.getValue();
    assertEquals("10", commandToClient.get("routes").getAsJsonArray()
        .get(0).getAsJsonObject().get("id").getAsString());
  }
}
