package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class StartCommandTest {

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
   * Test command for start.
   */
  @Test
  public void testStart() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    VisualTransitSimulator v = new VisualTransitSimulator(getClass().getClassLoader()
        .getResource("config.txt").getFile(), webServerSessionSpy);
    VisualTransitSimulator vtsSpy = spy(v);
    doNothing().when(vtsSpy).setVehicleFactories(Mockito.isA(Integer.class));
    doNothing().when(vtsSpy).start(Mockito.any(), Mockito.isA(Integer.class));
    JsonObject commandFromClient = new JsonObject();
    JsonArray timeBetweenVehicles = new JsonArray();
    timeBetweenVehicles.add(2);
    timeBetweenVehicles.add(3);
    commandFromClient.addProperty("command", "start");
    commandFromClient.addProperty("numTimeSteps", 50);
    commandFromClient.add("timeBetweenVehicles", timeBetweenVehicles);
    StartCommand startCommand = new StartCommand(vtsSpy);
    ArgumentCaptor<List<Integer>> listCaptor = ArgumentCaptor.forClass((Class) List.class);
    ArgumentCaptor<Integer> timeStepCaptor = ArgumentCaptor.forClass(Integer.class);
    startCommand.execute(webServerSessionSpy, commandFromClient);
    verify(vtsSpy).start(listCaptor.capture(), timeStepCaptor.capture());
    int timeStep = timeStepCaptor.getValue();
    List<Integer> timeList = listCaptor.getValue();
    assertEquals(50, timeStep);
    assertEquals(2, timeList.get(0));
    assertEquals(3, timeList.get(1));
  }
}
