package com.example.parkingcontrol.controllers;

import com.example.parkingcontrol.dtos.CarDTO;
import com.example.parkingcontrol.models.CarModel;
import com.example.parkingcontrol.services.CarService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/cars")
public class CarController {
    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity<Object> saveCar(@RequestBody @Valid CarDTO carDTO) {
        if (carService.existsByLicensePlate(carDTO.getLicensePlate())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License plate is already in use!");
        }

        CarModel carModel = new CarModel();
        BeanUtils.copyProperties(carDTO, carModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(carService.save(carModel));
    }

    @GetMapping
    public ResponseEntity<List<CarModel>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(carService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") UUID id) {
        Optional<CarModel> carModelOptional = carService.findById(id);
        if (!carModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(carModelOptional.get());
    }

    @GetMapping("/license-plate")
    public ResponseEntity<Object> findByLicensePlate(@RequestParam(defaultValue = "") String number) {
        Optional<CarModel> carModelOptional = carService.findByLicensePlate(number);
        if (!carModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(carModelOptional.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid CarDTO carDTO) {
        Optional<CarModel> carModelOptional = carService.findById(id);

        if (!carModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }

        CarModel carModel = carModelOptional.get();
        carModel.setCarBrand(carModel.getCarBrand());
        carModel.setCarModel(carModel.getCarModel());
        carModel.setCarColor(carModel.getCarColor());
        carModel.setLicensePlate(carModel.getLicensePlate());

        return ResponseEntity.status(HttpStatus.OK).body(carService.save(carModel));
    }
}
