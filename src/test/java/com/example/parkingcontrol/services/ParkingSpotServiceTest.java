package com.example.parkingcontrol.services;


import com.example.parkingcontrol.models.ParkingSpotModel;
import com.example.parkingcontrol.repositories.ParkingSpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParkingSpotServiceTest {
    @Mock
    private ParkingSpotRepository parkingSpotRepository;
    private ParkingSpotService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ParkingSpotService(parkingSpotRepository);
    }

    @Test
    void shouldSaveNewParkingSpot() {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(UUID.randomUUID());
        parkingSpot.setSpotNumber("701-A");
        parkingSpot.setApartment("701");
        parkingSpot.setBlock("I");
        parkingSpot.setOwner("Jade");
        parkingSpot.setRegistrationDate(LocalDateTime.parse("2022-09-03T10:15:30"));

        // When
        underTest.save(parkingSpot);

        // Then
        ArgumentCaptor<ParkingSpotModel> parkingSpotModelArgumentCaptor = ArgumentCaptor.forClass(ParkingSpotModel.class);

        verify(parkingSpotRepository).save(parkingSpotModelArgumentCaptor.capture());

        ParkingSpotModel capturedParkingSpot = parkingSpotModelArgumentCaptor.getValue();

        assertThat(capturedParkingSpot).isEqualTo(parkingSpot);
    }

    @Test
    void shouldCheckIfSpotNumberExists() {
        // When
        underTest.existsBySpotNumber(anyString());

        // Then
        verify(parkingSpotRepository).existsBySpotNumber(anyString());
    }

    @Test
    void shouldCheckIfApartmentAndBlockExists() {
        // When
        underTest.existsByApartmentAndBlock(anyString(), anyString());

        // Then
        verify(parkingSpotRepository).existsByApartmentAndBlock(anyString(), anyString());
    }

    @Test
    void shouldFindAllParkingSpots() {
        // When
        underTest.findAll();

        // Then
        verify(parkingSpotRepository).findAll();
    }

    @Test
    void shouldFindParkingSpotById() {
        // When
        UUID id = UUID.randomUUID();
        underTest.findById(id);

        // Then
        verify(parkingSpotRepository).findById(id);
    }

    @Test
    void shouldFindParkingSpotBySpotNumber() {
        // When
        String parkingSpotNumber = "701-A";
        underTest.findByParkingSpotNumber(parkingSpotNumber);

        // Then
        verify(parkingSpotRepository).findByParkingSpotNumber(parkingSpotNumber);
    }

    @Test
    void shouldFindParkingSpotByApartment() {
        // When
        String apartment = "701";
        underTest.findByApartment(apartment);

        // Then
        verify(parkingSpotRepository).findByApartment(apartment);
    }

    @Test
    void shouldFindParkingSpotByOwner() {
        // When
        String owner = "Jade";
        underTest.findByOwner(owner);

        // Then
        verify(parkingSpotRepository).findByOwnerIgnoreCase(owner);
    }

    @Test
    void shouldUpdateOneField() {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb"));
        parkingSpot.setSpotNumber("701-A");
        parkingSpot.setApartment("701");
        parkingSpot.setBlock("I");
        parkingSpot.setOwner("Jade");
        parkingSpot.setRegistrationDate(LocalDateTime.parse("2022-09-03T10:15:30"));

        // When
        ParkingSpotModel parkingSpotUpdateRequest = new ParkingSpotModel();
        parkingSpotUpdateRequest.setOwner("Billy");

        parkingSpot.setOwner(parkingSpotUpdateRequest.getOwner());

        underTest.save(parkingSpot);

        // Then
        ArgumentCaptor<ParkingSpotModel> parkingSpotModelArgumentCaptor = ArgumentCaptor.forClass(ParkingSpotModel.class);

        verify(parkingSpotRepository).save(parkingSpotModelArgumentCaptor.capture());

        ParkingSpotModel capturedParkingSpot = parkingSpotModelArgumentCaptor.getValue();

        assertThat(capturedParkingSpot.getOwner()).isEqualTo(parkingSpotUpdateRequest.getOwner());
    }

    @Test
    void shouldUpdateAllFields() {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb"));
        parkingSpot.setSpotNumber("701-A");
        parkingSpot.setApartment("701");
        parkingSpot.setBlock("I");
        parkingSpot.setOwner("Jade");
        parkingSpot.setRegistrationDate(LocalDateTime.parse("2022-09-03T10:15:30"));

        // When
        ParkingSpotModel parkingSpotUpdateRequest = new ParkingSpotModel();
        parkingSpotUpdateRequest.setId(parkingSpot.getId());
        parkingSpotUpdateRequest.setSpotNumber("666-A");
        parkingSpotUpdateRequest.setApartment("666");
        parkingSpotUpdateRequest.setBlock("II");
        parkingSpotUpdateRequest.setOwner("Billy");
        parkingSpotUpdateRequest.setRegistrationDate(parkingSpot.getRegistrationDate());

        BeanUtils.copyProperties(parkingSpotUpdateRequest, parkingSpot);

        underTest.save(parkingSpot);

        // Then
        ArgumentCaptor<ParkingSpotModel> parkingSpotModelArgumentCaptor = ArgumentCaptor.forClass(ParkingSpotModel.class);

        verify(parkingSpotRepository).save(parkingSpotModelArgumentCaptor.capture());

        ParkingSpotModel capturedParkingSpot = parkingSpotModelArgumentCaptor.getValue();

        assertThat(capturedParkingSpot).usingRecursiveComparison().isEqualTo(parkingSpotUpdateRequest);
    }

    @Test
    void shouldFindByIdAndDelete() {
        // When
        UUID id = UUID.randomUUID();
        underTest.delete(id);

        // Then
        verify(parkingSpotRepository).deleteById(id);
    }
}