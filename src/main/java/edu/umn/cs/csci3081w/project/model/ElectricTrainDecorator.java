package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;
import java.io.PrintStream;

public class ElectricTrainDecorator extends VehicleDecorator {

  public ElectricTrainDecorator(Vehicle vehicle) {
    super(vehicle);
  }

  /**
   * Adds a yellow color for electric train.
   *
   * @return The color yellow
   */
  @Override
  public Color getRgb() {
    if (vehicle.getLine().isIssueExist()) {
      return new Color(255, 204, 51, 155);
    }
    return new Color(255, 204, 51, 255);
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
