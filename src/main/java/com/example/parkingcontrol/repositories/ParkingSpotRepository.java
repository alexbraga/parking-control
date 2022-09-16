package com.example.parkingcontrol.repositories;

import com.example.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID> {
    Optional<ParkingSpotModel> findByParkingSpotNumber(String parkingSpotNumber);

    Optional<ParkingSpotModel> findByApartment(String apartment);

    Optional<ParkingSpotModel> findByOwner(String owner);
}
