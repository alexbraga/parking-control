package com.example.parkingcontrol.controllers;

import com.example.parkingcontrol.dtos.ParkingSpotDTO;
import com.example.parkingcontrol.models.CarModel;
import com.example.parkingcontrol.models.ParkingSpotModel;
import com.example.parkingcontrol.services.CarService;
import com.example.parkingcontrol.services.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {
    @Autowired
    ParkingSpotService parkingSpotService;

    @Autowired
    CarService carService;

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDTO) {
        if (parkingSpotService.existsBySpotNumber(parkingSpotDTO.getSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }

        if (parkingSpotService.existsByApartmentAndBlock(parkingSpotDTO.getApartment(), parkingSpotDTO.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Conflict: Parking Spot is already registered for this apartment and block!");
        }

        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDTO, parkingSpot);
        parkingSpot.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpot));
    }

    @PostMapping("/car/{carId}")
    public ResponseEntity<Object> saveParkingSpotAndCar(
            @PathVariable(value = "carId") UUID id,
            @RequestBody @Valid ParkingSpotDTO parkingSpotDTO
    ) {
        Optional<CarModel> carModelOptional = carService.findById(id);

        if (!carModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }

        if (parkingSpotService.existsBySpotNumber(parkingSpotDTO.getSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }

        if (parkingSpotService.existsByApartmentAndBlock(parkingSpotDTO.getApartment(), parkingSpotDTO.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Conflict: Parking Spot is already registered for this apartment and block!");
        }

        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDTO, parkingSpot);
        parkingSpot.setCar(carModelOptional.get());
        parkingSpot.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpot));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpotModel>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @GetMapping("/spot-number")
    public ResponseEntity<Object> findBySpotNumber(@RequestParam(defaultValue = "") String spot) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findBySpotNumber(spot);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @GetMapping("/apartment")
    public ResponseEntity<Object> findByApartment(@RequestParam(defaultValue = "") String number) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findByApartment(number);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findByOwner(@RequestParam(defaultValue = "") String name) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findByOwner(name);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid ParkingSpotDTO parkingSpotDTO) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);

        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found.");
        }

        CarModel carModel = new CarModel();
        carModel.setId(parkingSpotModelOptional.get().getCar().getId());
        BeanUtils.copyProperties(parkingSpotDTO.getCar(), carModel);

        ParkingSpotModel parkingSpot = parkingSpotModelOptional.get();
        BeanUtils.copyProperties(parkingSpotDTO, parkingSpot);
        parkingSpot.setId(parkingSpotModelOptional.get().getId());
        parkingSpot.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        parkingSpot.setCar(carModel);

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpot));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);

        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found.");
        }

        parkingSpotService.delete(parkingSpotModelOptional.get().getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Parking spot deleted successfully.");
    }
}
