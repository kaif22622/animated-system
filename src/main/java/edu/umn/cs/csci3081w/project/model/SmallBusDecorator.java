package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;
import java.io.PrintStream;

public class SmallBusDecorator extends VehicleDecorator {
  public SmallBusDecorator(Vehicle vehicle) {
    super(vehicle);
  }

  /**
   * Adds a maroon color to a small bus.
   *
   * @return The color maroon
   */
  @Override
  public Color getRgb() {
    if (vehicle.getLine().isIssueExist()) {
      return new Color(122, 0, 25, 155);
    }
    return new Color(122, 0, 25, 255);
  }

  @Override
  public void report(PrintStream out) {
    vehicle.report(out);
  }

  @Override
  public int getCurrentCO2Emission() {
    return vehicle.getCurrentCO2Emission();
  }
}
