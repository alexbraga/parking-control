package com.example.parkingcontrol.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CarDTO {
    @NotBlank
    @Size(min = 7, max = 8)
    private String licensePlate;
    @NotBlank
    private String carBrand;
    @NotBlank
    private String carModel;
    @NotBlank
    private String carColor;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }
}
