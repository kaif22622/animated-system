package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;
import java.io.PrintStream;

public class DieselTrainDecorator extends VehicleDecorator {

  public DieselTrainDecorator(Vehicle vehicle) {
    super(vehicle);
  }

  /**
   * Adds a green color to a diesel train.
   *
   * @return The color green
   */
  @Override
  public Color getRgb() {
    if (vehicle.getLine().isIssueExist()) {
      return new Color(60, 179, 113, 155);
    }
    return new Color(60, 179, 113, 255);
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
