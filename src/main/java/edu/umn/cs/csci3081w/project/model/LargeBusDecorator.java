package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;
import java.io.PrintStream;

public class LargeBusDecorator extends VehicleDecorator {
  public LargeBusDecorator(Vehicle vehicle) {
    super(vehicle);
  }

  /**
   * Adds a pink color to a large bus.
   *
   * @return The color pink
   */
  @Override
  public Color getRgb() {
    if (vehicle.getLine().isIssueExist()) {
      return new Color(239, 130, 238, 155);
    }
    return new Color(239, 130, 238, 255);
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
