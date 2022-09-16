package com.example.parkingcontrol.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class CarDTOTest {
    private Validator validator;

    @BeforeEach
    void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void hasNoNullFields() {
        // Given
        CarDTO car = new CarDTO();
        car.setCarBrand("Toyota");
        car.setCarModel("Etios");
        car.setCarColor("Silver");
        car.setLicensePlate("ABC-1234");

        // Then
        assertThat(car).hasNoNullFieldsOrProperties();
    }

    @Test
    void invalidCarBrandShouldFailValidation() {
        // Given
        CarDTO car = new CarDTO();
        car.setCarBrand(null);
        car.setCarModel("Etios");
        car.setCarColor("Silver");
        car.setLicensePlate("ABC-1234");

        // Then
        Set<ConstraintViolation<CarDTO>> violations = validator.validate(car);
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    void invalidCarModelShouldFailValidation() {
        // Given
        CarDTO car = new CarDTO();
        car.setCarBrand("Toyota");
        car.setCarModel(null);
        car.setCarColor("Silver");
        car.setLicensePlate("ABC-1234");

        // Then
        Set<ConstraintViolation<CarDTO>> violations = validator.validate(car);
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    void invalidCarColorShouldFailValidation() {
        // Given
        CarDTO car = new CarDTO();
        car.setCarBrand("Toyota");
        car.setCarModel("Etios");
        car.setCarColor(null);
        car.setLicensePlate("ABC-1234");

        // Then
        Set<ConstraintViolation<CarDTO>> violations = validator.validate(car);
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    void invalidLicensePlateShouldFailValidation() {
        // Given
        CarDTO car = new CarDTO();
        car.setCarBrand("Toyota");
        car.setCarModel("Etios");
        car.setCarColor("Silver");
        car.setLicensePlate("ABC123");

        // Then
        Set<ConstraintViolation<CarDTO>> violations = validator.validate(car);
        assertThat(violations.isEmpty()).isFalse();
    }
}