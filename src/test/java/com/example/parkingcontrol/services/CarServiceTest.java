package com.example.parkingcontrol.services;

import com.example.parkingcontrol.models.CarModel;
import com.example.parkingcontrol.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    CarRepository carRepository;

    private CarService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CarService(carRepository);
    }

    @Test
    void canSaveNewCar() {
        // Given
        CarModel carModel = new CarModel();
        carModel.setId(UUID.randomUUID());
        carModel.setCarBrand("Toyota");
        carModel.setCarModel("Etios");
        carModel.setCarColor("Silver");
        carModel.setLicensePlate("ABC-1234");

        // When
        underTest.save(carModel);

        // Then
        ArgumentCaptor<CarModel> carModelArgumentCaptor = ArgumentCaptor.forClass(CarModel.class);

        verify(carRepository).save(carModelArgumentCaptor.capture());

        CarModel capturedCarModel = carModelArgumentCaptor.getValue();

        assertThat(capturedCarModel).isEqualTo(carModel);
    }

    @Test
    void canFindAllCars() {
        // When
        underTest.findAll();

        // Then
        verify(carRepository).findAll();
    }

    @Test
    void canFindById() {
        // When
        UUID id = UUID.randomUUID();
        underTest.findById(id);

        // Then
        verify(carRepository).findById(id);
    }

    @Test
    void canFindByLicensePlate() {
        // When
        String licensePlate = "ABC-1234";
        underTest.findByLicensePlate(licensePlate);

        // Then
        verify(carRepository).findByLicensePlate(licensePlate);
    }

    @Test
    void canFindAndUpdateOneField() {
        // Given
        CarModel car = new CarModel();
        car.setId(UUID.randomUUID());
        car.setCarBrand("Toyota");
        car.setCarModel("Etios");
        car.setCarColor("Silver");
        car.setLicensePlate("ABC-1234");

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        // When
        CarModel carUpdateRequest = new CarModel();
        String color = "Black";
        carUpdateRequest.setCarColor(color);

        underTest.update(car.getId(), carUpdateRequest);

        // Then
        ArgumentCaptor<CarModel> carModelArgumentCaptor = ArgumentCaptor.forClass(CarModel.class);

        verify(carRepository).save(carModelArgumentCaptor.capture());

        CarModel capturedCar = carModelArgumentCaptor.getValue();

        assertThat(capturedCar.getCarColor()).isEqualTo(carUpdateRequest.getCarColor());
    }

    @Test
    void canFindAndUpdateAllFields() {
        // Given
        CarModel car = new CarModel();
        car.setId(UUID.randomUUID());
        car.setCarBrand("Toyota");
        car.setCarModel("Etios");
        car.setCarColor("Silver");
        car.setLicensePlate("ABC-1234");

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        // When
        CarModel carUpdateRequest = new CarModel();
        carUpdateRequest.setId(car.getId());
        carUpdateRequest.setCarModel("A1");
        carUpdateRequest.setCarBrand("Audi");
        carUpdateRequest.setCarColor("Black");
        carUpdateRequest.setLicensePlate("XYZ-9876");

        underTest.update(car.getId(), carUpdateRequest);

        // Then
        ArgumentCaptor<CarModel> carModelArgumentCaptor = ArgumentCaptor.forClass(CarModel.class);

        verify(carRepository).save(carModelArgumentCaptor.capture());

        CarModel capturedCar = carModelArgumentCaptor.getValue();

        assertThat(capturedCar).usingRecursiveComparison().isEqualTo(carUpdateRequest);
    }

    @Test
    void canFindByIdAndDelete() {
        // WHen
        UUID id = UUID.randomUUID();
        underTest.delete(id);

        // Then
        verify(carRepository).deleteById(id);
    }
}