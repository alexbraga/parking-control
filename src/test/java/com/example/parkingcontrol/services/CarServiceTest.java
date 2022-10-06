package com.example.parkingcontrol.services;

import com.example.parkingcontrol.models.CarModel;
import com.example.parkingcontrol.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

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
    void shouldSaveNewCar() {
        // Given
        CarModel carModel = new CarModel();
        carModel.setId(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c"));
        carModel.setBrand("Toyota");
        carModel.setModel("Etios");
        carModel.setColor("Silver");
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
    void shouldFindAllCars() {
        // When
        underTest.findAll();

        // Then
        verify(carRepository).findAll();
    }

    @Test
    void shouldCheckIfLicensePlateExists() {
        // When
        underTest.existsByLicensePlate(anyString());

        // Then
        verify(carRepository).existsByLicensePlate(anyString());
    }

    @Test
    void shouldFindById() {
        // When
        UUID id = UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c");
        underTest.findById(id);

        // Then
        verify(carRepository).findById(id);
    }

    @Test
    void shouldFindByLicensePlate() {
        // When
        String licensePlate = "ABC-1234";
        underTest.findByLicensePlate(licensePlate);

        // Then
        verify(carRepository).findByLicensePlate(licensePlate);
    }

    @Test
    void shouldUpdateOneField() {
        // Given
        CarModel car = new CarModel();
        car.setId(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c"));
        car.setBrand("Toyota");
        car.setModel("Etios");
        car.setColor("Silver");
        car.setLicensePlate("ABC-1234");

        // When
        CarModel carUpdateRequest = new CarModel();
        carUpdateRequest.setColor("Black");

        car.setColor(carUpdateRequest.getColor());

        underTest.save(car);

        // Then
        ArgumentCaptor<CarModel> carModelArgumentCaptor = ArgumentCaptor.forClass(CarModel.class);

        verify(carRepository).save(carModelArgumentCaptor.capture());

        CarModel capturedCar = carModelArgumentCaptor.getValue();

        assertThat(capturedCar.getColor()).isEqualTo(carUpdateRequest.getColor());
    }

    @Test
    void shouldUpdateAllFields() {
        // Given
        CarModel car = new CarModel();
        car.setId(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c"));
        car.setBrand("Toyota");
        car.setModel("Etios");
        car.setColor("Silver");
        car.setLicensePlate("ABC-1234");

        // When
        CarModel carUpdateRequest = new CarModel();
        carUpdateRequest.setId(car.getId());
        carUpdateRequest.setModel("A1");
        carUpdateRequest.setBrand("Audi");
        carUpdateRequest.setColor("Black");
        carUpdateRequest.setLicensePlate("XYZ-9876");

        BeanUtils.copyProperties(carUpdateRequest, car);

        underTest.save(car);

        // Then
        ArgumentCaptor<CarModel> carModelArgumentCaptor = ArgumentCaptor.forClass(CarModel.class);

        verify(carRepository).save(carModelArgumentCaptor.capture());

        CarModel capturedCar = carModelArgumentCaptor.getValue();

        assertThat(capturedCar).usingRecursiveComparison().isEqualTo(carUpdateRequest);
    }

    @Test
    void shouldFindByIdAndDelete() {
        // WHen
        UUID id = UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c");
        underTest.delete(id);

        // Then
        verify(carRepository).deleteById(id);
    }
}