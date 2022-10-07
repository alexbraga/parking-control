package com.example.parkingcontrol.dtos;

import javax.validation.constraints.NotBlank;

public class ParkingSpotDTO {
    @NotBlank
    private String spotNumber;

    @NotBlank
    private String owner;

    @NotBlank
    private String apartment;

    @NotBlank
    private String block;

    private CarDTO car;

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }
}
