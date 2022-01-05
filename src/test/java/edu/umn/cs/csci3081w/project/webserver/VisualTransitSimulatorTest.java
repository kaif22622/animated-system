package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.StorageFacility;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class VisualTransitSimulatorTest {

  private VisualTransitSimulator vts;
  private WebServerSession webServerSessionSpy;
  private List<Integer> vehicleStartTimings;
  private int numTimeSteps;

  /**
   * Sets up VisualTransitSimulator with a mock WebServerSession.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
    webServerSessionSpy = spy(WebServerSession.class);
    vts = new VisualTransitSimulator(getClass().getClassLoader()
        .getResource("config.txt").getFile(), webServerSessionSpy);
    vts.setVehicleFactories(5);
    vehicleStartTimings = new ArrayList<Integer>();
    vehicleStartTimings.add(0, 1);
    vehicleStartTimings.add(1, 1);
    numTimeSteps = 99;
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    vts.start(vehicleStartTimings, numTimeSteps);
  }

  /**
   * Tests the update command through 50 time steps.
   */
  @Test
  public void testUpdate() {
    vts.setLogging();
    for (int i = 0; i < numTimeSteps + 1; i++) {
      vts.update();
    }
    assertEquals(true, vts.getUpdatedLines());
  }

  /**
   * Test Visual Transit Simulator with a null storage facility,
   * where elapsed time is greater than the number of time steps.
   */
  @Test
  public void testNullStorageFacility() {
    vts = new VisualTransitSimulator("badpath", webServerSessionSpy);
    StorageFacility storageFacility = vts.getStorageFacility();
    assertEquals(Integer.MAX_VALUE, storageFacility.getSmallBusesNum());
    assertEquals(Integer.MAX_VALUE, storageFacility.getLargeBusesNum());
    assertEquals(Integer.MAX_VALUE, storageFacility.getDieselTrainsNum());
    assertEquals(Integer.MAX_VALUE, storageFacility.getElectricTrainsNum());
  }

  /**
   * Tests update where logging is set to true.
   */
  @Test
  public void testLogIsTrue() {
    vts.setLogging();
    vts.update();
    assertEquals(true, vts.getUpdatedLines());
  }
}
