package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrainFactoryTest {
  private StorageFacility storageFacility;
  private StorageFacility storageFacilityZero;
  private TrainFactory trainFactory;
  private TrainFactory trainFactoryNight4;
  private TrainFactory trainFactoryNight17;
  private TrainFactory trainFactoryZero;

  /**
   * Sets up the facility and factories.
   */
  @BeforeEach
  public void setUp() {
    storageFacility = new StorageFacility(0, 0, 3, 3);
    storageFacilityZero = new StorageFacility(0, 0, 0, 0);
    trainFactory = new TrainFactory(storageFacility, new Counter(), 9);
    trainFactoryNight4 = new TrainFactory(storageFacility, new Counter(), 4);
    trainFactoryNight17 = new TrainFactory(storageFacility, new Counter(), 17);
    trainFactoryZero = new TrainFactory(storageFacilityZero, new Counter(), 9);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testConstructor() {
    assertTrue(trainFactory.getGenerationStrategy() instanceof TrainStrategyDay);
    assertTrue(trainFactoryNight4.getGenerationStrategy() instanceof TrainStrategyNight);
    assertTrue(trainFactoryNight17.getGenerationStrategy() instanceof TrainStrategyNight);
  }

  /**
   * Testing if generated vehicle is working according to strategy.
   */
  @Test
  public void testGenerateVehicle() {
    List<Stop> stopsIn = new ArrayList<Stop>();
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    stopsIn.add(stop1);
    stopsIn.add(stop2);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.843774422231134);
    List<Double> probabilitiesIn = new ArrayList<Double>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);

    Route testRouteIn = new Route(0, "testRouteIn",
        stopsIn, distancesIn, generatorIn);

    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop2);
    stopsOut.add(stop1);
    List<Double> distancesOut = new ArrayList<>();
    distancesOut.add(0.843774422231134);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.025);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);

    Route testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Line line = new Line(10000, "testLine", "TRAIN", testRouteOut, testRouteIn,
        new Issue());

    Vehicle vehicle1 = trainFactory.generateVehicle(line);
    assertTrue(vehicle1 instanceof ElectricTrainDecorator);
    Vehicle vehicle2 = trainFactoryNight4.generateVehicle(line);
    assertTrue(vehicle2 instanceof ElectricTrainDecorator);
    Vehicle vehicle3 = trainFactoryNight4.generateVehicle(line);
    assertTrue(vehicle3 instanceof DieselTrainDecorator);
    Vehicle vehicle4 = trainFactoryZero.generateVehicle(line);
    assertEquals(null, vehicle4);


  }

  /**
   * Testing if vehicle got returned.
   */
  @Test
  public void testReturnVehicleElectricTrain() {
    List<Stop> stopsIn = new ArrayList<Stop>();
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    stopsIn.add(stop1);
    stopsIn.add(stop2);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.843774422231134);
    List<Double> probabilitiesIn = new ArrayList<Double>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);

    Route testRouteIn = new Route(0, "testRouteIn",
        stopsIn, distancesIn, generatorIn);

    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop2);
    stopsOut.add(stop1);
    List<Double> distancesOut = new ArrayList<>();
    distancesOut.add(0.843774422231134);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.025);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);

    Route testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Train testTrain = new ElectricTrain(1, new Line(10000, "testLine", "ELECTRIC_TRAIN",
        testRouteOut, testRouteIn, new Issue()), 3, 1.0);

    assertEquals(3, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(3, trainFactory.getStorageFacility().getDieselTrainsNum());
    trainFactory.returnVehicle(testTrain);
    assertEquals(4, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(3, trainFactory.getStorageFacility().getDieselTrainsNum());

    Vehicle testElectricDecorator = new ElectricTrainDecorator(testTrain);
    assertEquals(4, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(3, trainFactory.getStorageFacility().getDieselTrainsNum());
    trainFactory.returnVehicle(testElectricDecorator);
    assertEquals(5, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(3, trainFactory.getStorageFacility().getDieselTrainsNum());

    Train testTrainDiesel = new DieselTrain(1, new Line(10000, "testLine", "DIESEL_TRAIN",
        testRouteOut, testRouteIn, new Issue()), 3, 1.0);

    assertEquals(5, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(3, trainFactory.getStorageFacility().getDieselTrainsNum());
    trainFactory.returnVehicle(testTrainDiesel);
    assertEquals(5, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(4, trainFactory.getStorageFacility().getDieselTrainsNum());

    Vehicle testDieselDecorator = new DieselTrainDecorator(testTrainDiesel);

    assertEquals(5, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(4, trainFactory.getStorageFacility().getDieselTrainsNum());
    trainFactory.returnVehicle(testDieselDecorator);
    assertEquals(5, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(5, trainFactory.getStorageFacility().getDieselTrainsNum());

    Bus smallBus = new SmallBus(1000, new Line(2, "testLine1", "BUS",
        testRouteOut, testRouteIn, new Issue()), 3, 1.0);

    assertEquals(5, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(5, trainFactory.getStorageFacility().getDieselTrainsNum());
    trainFactory.returnVehicle(smallBus);
    assertEquals(5, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(5, trainFactory.getStorageFacility().getDieselTrainsNum());
  }
}
