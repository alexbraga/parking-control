package com.example.parkingcontrol.dtos;

import javax.validation.constraints.NotBlank;

public class ParkingSpotDTO {
    @NotBlank
    private String parkingSpotNumber;
    @NotBlank
    private String owner;
    @NotBlank
    private String apartment;
    @NotBlank
    private String block;
}
