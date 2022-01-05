package edu.umn.cs.csci3081w.project.model;

public abstract class VehicleDecorator extends Vehicle {
  protected Vehicle vehicle;

  /**
   * Constructs vehicle with color.
   *
   * @param vehicle A vehicle that is either a small bus, large bus, electric train, or diesel train
   */
  public VehicleDecorator(Vehicle vehicle) {
    super(vehicle.getId(), vehicle.getLine(), vehicle.getCapacity(),
        vehicle.getSpeed(), vehicle.getPassengerLoader(), vehicle.getPassengerUnloader());
    this.vehicle = vehicle;
  }
}

