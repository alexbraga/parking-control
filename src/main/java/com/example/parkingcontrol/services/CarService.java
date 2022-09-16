package com.example.parkingcontrol.services;

import com.example.parkingcontrol.models.CarModel;
import com.example.parkingcontrol.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarService {
    @Autowired
    CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional
    public void save(CarModel carModel) {
        carRepository.save(carModel);
    }

    public List<CarModel> findAll() {
        return carRepository.findAll();
    }

    public Optional<CarModel> findByLicensePlate(String licensePlate) {
        return carRepository.findByLicensePlate(licensePlate);
    }

    public Optional<CarModel> findById(UUID id) {
        return carRepository.findById(id);
    }

    @Transactional
    public void update(UUID id, CarModel carUpdateRequest) {
        Optional<CarModel> carToUpdate = carRepository.findById(id);
        CarModel car = carToUpdate.get();

        car.setCarColor(carUpdateRequest.getCarColor());
        car.setCarBrand(carUpdateRequest.getCarBrand());
        car.setCarModel(carUpdateRequest.getCarModel());
        car.setLicensePlate(carUpdateRequest.getLicensePlate());

        carRepository.save(car);
    }

    @Transactional
    public void delete(UUID id) {
        carRepository.deleteById(id);
    }
}
