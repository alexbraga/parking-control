package com.example.parkingcontrol.repositories;

import com.example.parkingcontrol.models.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<CarModel, UUID> {

    Optional<CarModel> findByLicensePlate(String licensePlate);

    boolean existsByLicensePlate(String licensePlate);
}
