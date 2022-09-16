package com.example.parkingcontrol.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ParkingSpotDTOTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void hasNoNullFields() {
        // Given
        ParkingSpotDTO parkingSpot = new ParkingSpotDTO();
        parkingSpot.setParkingSpotNumber("701-A");
        parkingSpot.setApartment("701");
        parkingSpot.setBlock("I");
        parkingSpot.setOwner("Jade");

        // Then
        assertThat(parkingSpot).hasNoNullFieldsOrProperties();
    }

    @Test
    void invalidParkingSpotNumberShouldFailValidation() {
        // Given
        ParkingSpotDTO parkingSpot = new ParkingSpotDTO();
        parkingSpot.setParkingSpotNumber(null);
        parkingSpot.setApartment("701");
        parkingSpot.setBlock("I");
        parkingSpot.setOwner("Jade");

        // Then
        Set<ConstraintViolation<ParkingSpotDTO>> violations = validator.validate(parkingSpot);
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    void invalidApartmentShouldFailValidation() {
        // Given
        ParkingSpotDTO parkingSpot = new ParkingSpotDTO();
        parkingSpot.setParkingSpotNumber("701-A");
        parkingSpot.setApartment(null);
        parkingSpot.setBlock("I");
        parkingSpot.setOwner("Jade");

        // Then
        Set<ConstraintViolation<ParkingSpotDTO>> violations = validator.validate(parkingSpot);
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    void invalidBlockShouldFailValidation() {
        // Given
        ParkingSpotDTO parkingSpot = new ParkingSpotDTO();
        parkingSpot.setParkingSpotNumber("701-A");
        parkingSpot.setApartment("701");
        parkingSpot.setBlock(null);
        parkingSpot.setOwner("Jade");

        // Then
        Set<ConstraintViolation<ParkingSpotDTO>> violations = validator.validate(parkingSpot);
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    void invalidOwnerShouldFailValidation() {
        // Given
        ParkingSpotDTO parkingSpot = new ParkingSpotDTO();
        parkingSpot.setParkingSpotNumber("71-A");
        parkingSpot.setApartment("701");
        parkingSpot.setBlock("I");
        parkingSpot.setOwner(null);

        // Then
        Set<ConstraintViolation<ParkingSpotDTO>> violations = validator.validate(parkingSpot);
        assertThat(violations.isEmpty()).isFalse();
    }
}