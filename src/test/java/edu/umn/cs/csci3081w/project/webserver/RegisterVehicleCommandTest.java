package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class RegisterVehicleCommandTest {

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
   * Test command for register vehicle.
   */
  @Test
  public void testRegisterVehicle() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    VisualTransitSimulator v = new VisualTransitSimulator(getClass().getClassLoader()
        .getResource("config.txt").getFile(), webServerSessionSpy);
    List<Integer> timeBetweenVehicles = new ArrayList<>();
    timeBetweenVehicles.add(2);
    timeBetweenVehicles.add(3);
    v.start(timeBetweenVehicles, 50);
    v.setVehicleFactories(10);
    v.update();
    VisualTransitSimulator vtsSpy = spy(v);
    doNothing().when(vtsSpy).addObserver(Mockito.isA(Vehicle.class));
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "registerVehicle");
    commandFromClient.addProperty("id", 2000); //want the vehicle with id 2000 to be registered
    RegisterVehicleCommand registerVehicleCommand = new RegisterVehicleCommand(vtsSpy);
    ArgumentCaptor<Vehicle> vehicleCaptor = ArgumentCaptor.forClass(Vehicle.class);
    registerVehicleCommand.execute(webServerSessionSpy, commandFromClient);
    verify(vtsSpy).addObserver(vehicleCaptor.capture());
    Vehicle observedVehicle = vehicleCaptor.getValue();
    assertEquals(2000, observedVehicle.getId());
  }
}
