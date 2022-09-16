package com.example.parkingcontrol.services;


import com.example.parkingcontrol.models.ParkingSpotModel;
import com.example.parkingcontrol.repositories.ParkingSpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void canSaveNewParkingSpot() {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(UUID.randomUUID());
        parkingSpot.setParkingSpotNumber("701-A");
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
    void canFindAllParkingSpots() {
        // When
        underTest.findAll();

        // Then
        verify(parkingSpotRepository).findAll();
    }

    @Test
    void canFindParkingSpotById() {
        // When
        UUID id = UUID.randomUUID();
        underTest.findById(id);

        // Then
        verify(parkingSpotRepository).findById(id);
    }

    @Test
    void canFindParkingSpotBySpotNumber() {
        // When
        String parkingSpotNumber = "701-A";
        underTest.findByParkingSpotNumber(parkingSpotNumber);

        // Then
        verify(parkingSpotRepository).findByParkingSpotNumber(parkingSpotNumber);
    }

    @Test
    void canFindParkingSpotByApartment() {
        // When
        String apartment = "701";
        underTest.findByApartment(apartment);

        // Then
        verify(parkingSpotRepository).findByApartment(apartment);
    }

    @Test
    void canFindParkingSpotByOwner() {
        // When
        String owner = "Jade";
        underTest.findByOwner(owner);

        // Then
        verify(parkingSpotRepository).findByOwner(owner);
    }

    @Test
    void canFindByIdAndUpdateOneField() {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(UUID.randomUUID());
        parkingSpot.setParkingSpotNumber("701-A");
        parkingSpot.setApartment("701");
        parkingSpot.setBlock("I");
        parkingSpot.setOwner("Jade");
        parkingSpot.setRegistrationDate(LocalDateTime.parse("2022-09-03T10:15:30"));

        when(parkingSpotRepository.findById(parkingSpot.getId())).thenReturn(Optional.of(parkingSpot));

        // When
        ParkingSpotModel ParkingSpotUpdateRequest = new ParkingSpotModel();
        String owner = "Billy";
        ParkingSpotUpdateRequest.setOwner(owner);
        underTest.update(parkingSpot.getId(), ParkingSpotUpdateRequest);

        // Then
        ArgumentCaptor<ParkingSpotModel> parkingSpotModelArgumentCaptor = ArgumentCaptor.forClass(ParkingSpotModel.class);

        verify(parkingSpotRepository).save(parkingSpotModelArgumentCaptor.capture());

        ParkingSpotModel capturedParkingSpot = parkingSpotModelArgumentCaptor.getValue();

        assertThat(capturedParkingSpot.getOwner()).isEqualTo(ParkingSpotUpdateRequest.getOwner());
    }

    @Test
    void canFindByIdAndUpdateAllFields() {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(UUID.randomUUID());
        parkingSpot.setParkingSpotNumber("701-A");
        parkingSpot.setApartment("701");
        parkingSpot.setBlock("I");
        parkingSpot.setOwner("Jade");
        parkingSpot.setRegistrationDate(LocalDateTime.parse("2022-09-03T10:15:30"));

        when(parkingSpotRepository.findById(parkingSpot.getId())).thenReturn(Optional.of(parkingSpot));

        // When
        ParkingSpotModel parkingSpotUpdateRequest = new ParkingSpotModel();
        parkingSpotUpdateRequest.setId(parkingSpot.getId());
        parkingSpotUpdateRequest.setParkingSpotNumber("666-A");
        parkingSpotUpdateRequest.setApartment("666");
        parkingSpotUpdateRequest.setBlock("II");
        parkingSpotUpdateRequest.setOwner("Billy");
        parkingSpotUpdateRequest.setRegistrationDate(parkingSpot.getRegistrationDate());

        underTest.update(parkingSpot.getId(), parkingSpotUpdateRequest);

        // Then
        ArgumentCaptor<ParkingSpotModel> parkingSpotModelArgumentCaptor = ArgumentCaptor.forClass(ParkingSpotModel.class);

        verify(parkingSpotRepository).save(parkingSpotModelArgumentCaptor.capture());

        ParkingSpotModel capturedParkingSpot = parkingSpotModelArgumentCaptor.getValue();

        assertThat(capturedParkingSpot).usingRecursiveComparison().isEqualTo(parkingSpotUpdateRequest);
    }

    @Test
    void canFindByIdAndDelete() {
        // When
        UUID id = UUID.randomUUID();
        underTest.delete(id);

        // Then
        verify(parkingSpotRepository).deleteById(id);
    }
}