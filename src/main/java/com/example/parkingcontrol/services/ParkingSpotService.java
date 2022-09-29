package com.example.parkingcontrol.services;

import com.example.parkingcontrol.models.ParkingSpotModel;
import com.example.parkingcontrol.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {
    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpot) {
        return parkingSpotRepository.save(parkingSpot);
    }

    public List<ParkingSpotModel> findAll() {
        return parkingSpotRepository.findAll();
    }

    public Optional<ParkingSpotModel> findById(UUID id) {
        return parkingSpotRepository.findById(id);
    }

    public Optional<ParkingSpotModel> findByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.findByParkingSpotNumber(parkingSpotNumber);
    }

    public Optional<ParkingSpotModel> findByApartment(String apartment) {
        return parkingSpotRepository.findByApartment(apartment);
    }

    public Optional<ParkingSpotModel> findByOwner(String owner) {
        return parkingSpotRepository.findByOwnerIgnoreCase(owner);
    }

    @Transactional
    public void delete(UUID id) {
        parkingSpotRepository.deleteById(id);
    }
}
