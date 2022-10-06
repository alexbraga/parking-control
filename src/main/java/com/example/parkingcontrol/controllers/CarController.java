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
}
