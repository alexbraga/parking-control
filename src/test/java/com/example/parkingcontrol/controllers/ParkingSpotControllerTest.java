package com.example.parkingcontrol.controllers;

import com.example.parkingcontrol.dtos.CarDTO;
import com.example.parkingcontrol.dtos.ParkingSpotDTO;
import com.example.parkingcontrol.models.CarModel;
import com.example.parkingcontrol.models.ParkingSpotModel;
import com.example.parkingcontrol.services.CarService;
import com.example.parkingcontrol.services.ParkingSpotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParkingSpotController.class)
@ActiveProfiles("test")
class ParkingSpotControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ParkingSpotService parkingSpotService;

    @MockBean
    CarService carService;

    @Test
    void shouldSaveNewParkingSpot() throws Exception {
        // Given
        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO();
        parkingSpotDTO.setSpotNumber("701-A");
        parkingSpotDTO.setApartment("701");
        parkingSpotDTO.setBlock("I");
        parkingSpotDTO.setOwner("Jade");

        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb"));

        BeanUtils.copyProperties(parkingSpotDTO, parkingSpot);

        // When
        Mockito.when(parkingSpotService.save(any(ParkingSpotModel.class))).thenReturn(parkingSpot);

        // Then
        mockMvc.perform(post("/parking-spot")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(parkingSpotDTO)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is("0a96e04e-b60f-4b69-9524-e221cf341ccb")));
    }

    @Test
    void shouldFailSavingWhenSpotNumberAlreadyInUse() throws Exception {
        // Given
        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO();
        parkingSpotDTO.setSpotNumber("701-A");
        parkingSpotDTO.setApartment("701");
        parkingSpotDTO.setBlock("I");
        parkingSpotDTO.setOwner("Jade");

        // When
        Mockito.when(parkingSpotService.existsBySpotNumber(parkingSpotDTO.getSpotNumber())).thenReturn(true);

        // Then
        mockMvc.perform(post("/parking-spot")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(parkingSpotDTO)))
               .andExpect(status().isConflict())
               .andExpect(content().string("Conflict: Parking Spot is already in use!"));
    }

    @Test
    void shouldFailSavingWhenApartmentAndBlockAlreadyInUse() throws Exception {
        // Given
        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO();
        parkingSpotDTO.setSpotNumber("701-A");
        parkingSpotDTO.setApartment("701");
        parkingSpotDTO.setBlock("I");
        parkingSpotDTO.setOwner("Jade");

        // When
        Mockito.when(parkingSpotService.existsByApartmentAndBlock(parkingSpotDTO.getApartment(), parkingSpotDTO.getBlock()))
               .thenReturn(true);

        // Then
        mockMvc.perform(post("/parking-spot")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(parkingSpotDTO)))
               .andExpect(status().isConflict())
               .andExpect(content().string("Conflict: Parking Spot is already registered for this apartment and block!"));
    }

    @Test
    void shouldSaveParkingSpotAndCar() throws Exception {
        // Given
        CarModel carModel = new CarModel();
        UUID carId = UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c");
        carModel.setId(carId);

        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO();
        parkingSpotDTO.setSpotNumber("701-A");
        parkingSpotDTO.setApartment("701");
        parkingSpotDTO.setBlock("I");
        parkingSpotDTO.setOwner("Jade");

        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        UUID parkingSpotId = UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb");
        parkingSpot.setId(parkingSpotId);
        parkingSpot.setCar(carModel);

        BeanUtils.copyProperties(parkingSpotDTO, parkingSpot);

        // When
        Mockito.when(carService.findById(carId)).thenReturn(Optional.of(carModel));
        Mockito.when(parkingSpotService.save(any(ParkingSpotModel.class))).thenReturn(parkingSpot);

        // Then
        mockMvc.perform(post("/parking-spot/car/" + carId)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(parkingSpotDTO)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is("0a96e04e-b60f-4b69-9524-e221cf341ccb")))
               .andExpect(jsonPath("car.id", is("3e01ec1b-85c1-4892-bf11-c02eca5b198c")));
    }

    @Test
    void shouldFailSavingParkingSpotAndCarWhenCarNotFound() throws Exception {
        // Given
        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO();
        parkingSpotDTO.setSpotNumber("701-A");
        parkingSpotDTO.setApartment("701");
        parkingSpotDTO.setBlock("I");
        parkingSpotDTO.setOwner("Jade");

        // When
        Mockito.when(carService.findById(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c")))
               .thenReturn(Optional.empty());

        // Then
        mockMvc.perform(post("/parking-spot/car/3e01ec1b-85c1-4892-bf11-c02eca5b198c")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(parkingSpotDTO)))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Car not found."));
    }

    @Test
    void shouldGetAllParkingSpots() throws Exception {
        mockMvc.perform(get("/parking-spot")).andExpect(status().isOk());
    }

    @Test
    void shouldFindParkingSpotById() throws Exception {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb"));

        // When
        Mockito.when(parkingSpotService.findById(UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb"))).thenReturn(
                Optional.of(parkingSpot));

        // Then
        mockMvc.perform(get("/parking-spot/0a96e04e-b60f-4b69-9524-e221cf341ccb")).andExpect(status().isOk());
    }

    @Test
    void shouldFailWhenParkingSpotNotFoundById() throws Exception {
        // When
        Mockito.when(parkingSpotService.findById(UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb"))).thenReturn(
                Optional.empty());

        // Then
        mockMvc.perform(get("/parking-spot/0a96e04e-b60f-4b69-9524-e221cf341ccb"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Parking spot not found."));
    }

    @Test
    void shouldFindParkingSpotBySpotNumber() throws Exception {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setSpotNumber("701-A");

        // When
        Mockito.when(parkingSpotService.findBySpotNumber("701-A")).thenReturn(Optional.of(parkingSpot));

        // Then
        mockMvc.perform(get("/parking-spot/spot-number?spot=701-A")).andExpect(status().isOk());
    }

    @Test
    void shouldFailWhenParkingSpotNotFoundBySpotNumber() throws Exception {
        // When
        Mockito.when(parkingSpotService.findBySpotNumber("701")).thenReturn(Optional.empty());

        // Then
        mockMvc.perform(get("/parking-spot/spot-number?spot=701"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Parking spot not found."));
    }

    @Test
    void shouldFindParkingSpotByApartment() throws Exception {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setApartment("701");

        // When
        Mockito.when(parkingSpotService.findByApartment("701")).thenReturn(Optional.of(parkingSpot));

        // Then
        mockMvc.perform(get("/parking-spot/apartment?number=701")).andExpect(status().isOk());
    }

    @Test
    void shouldFailWhenParkingSpotNotFoundByApartment() throws Exception {
        // When
        Mockito.when(parkingSpotService.findByApartment("603")).thenReturn(Optional.empty());

        // Then
        mockMvc.perform(get("/parking-spot/apartment?number=603"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Parking spot not found."));
    }

    @Test
    void shouldFindParkingSpotByOwner() throws Exception {
        // Given
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setOwner("Jade");

        // When
        Mockito.when(parkingSpotService.findByOwner("Jade")).thenReturn(Optional.of(parkingSpot));

        // Then
        mockMvc.perform(get("/parking-spot/owner?name=Jade")).andExpect(status().isOk());
    }

    @Test
    void shouldFailWhenParkingSpotNotFoundByOwner() throws Exception {
        // When
        Mockito.when(parkingSpotService.findByOwner("Jessica")).thenReturn(Optional.empty());

        // Then
        mockMvc.perform(get("/parking-spot/owner?name=Jessica"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Parking spot not found."));
    }

    @Test
    void shouldUpdateParkingSpot() throws Exception {
        // Given
        CarDTO carDTO = new CarDTO();
        carDTO.setBrand("Audi");
        carDTO.setModel("A1");
        carDTO.setColor("Silver");
        carDTO.setLicensePlate("GPK-6219");

        CarModel carModel = new CarModel();
        BeanUtils.copyProperties(carDTO, carModel);

        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO();
        parkingSpotDTO.setSpotNumber("701-A");
        parkingSpotDTO.setApartment("701");
        parkingSpotDTO.setBlock("I");
        parkingSpotDTO.setOwner("Jade");
        parkingSpotDTO.setCar(carDTO);

        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb"));
        parkingSpot.setCar(carModel);

        BeanUtils.copyProperties(parkingSpotDTO, parkingSpot);

        // When
        Mockito.when(parkingSpotService.findById(UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb")))
               .thenReturn(Optional.of(parkingSpot));

        Mockito.when(parkingSpotService.save(any(ParkingSpotModel.class))).thenReturn(parkingSpot);

        // Then
        mockMvc.perform(put("/parking-spot/update/0a96e04e-b60f-4b69-9524-e221cf341ccb")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(parkingSpotDTO)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is("0a96e04e-b60f-4b69-9524-e221cf341ccb")))
               .andExpect(content().string(objectMapper.writeValueAsString(parkingSpot)));
    }

    @Test
    void shouldFailUpdateWhenInvalidParkingSpotDTOSubmitted() throws Exception {
        // Given
        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO();
        parkingSpotDTO.setSpotNumber("");
        parkingSpotDTO.setApartment("701");
        parkingSpotDTO.setBlock("I");
        parkingSpotDTO.setOwner("Jade");

        // Then
        mockMvc.perform(put("/parking-spot/update/0a96e04e-b60f-4b69-9524-e221cf341ccb")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(parkingSpotDTO)))
               .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailUpdateWhenParkingSpotNotFound() throws Exception {
        // Given
        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO();
        parkingSpotDTO.setSpotNumber("701-A");
        parkingSpotDTO.setApartment("701");
        parkingSpotDTO.setBlock("I");
        parkingSpotDTO.setOwner("Jade");

        // Then
        mockMvc.perform(put("/parking-spot/update/0a96e04e-b60f-4b69-9524-e221cf341ccb")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(parkingSpotDTO)))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Parking spot not found."));
    }

    @Test
    void shouldDeleteParkingSpot() throws Exception {
        // Given
        UUID id = UUID.fromString("0a96e04e-b60f-4b69-9524-e221cf341ccb");
        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setId(id);

        ParkingSpotService serviceSpy = Mockito.spy(parkingSpotService);

        // When
        Mockito.when(parkingSpotService.findById(id)).thenReturn(Optional.of(parkingSpot));
        Mockito.doNothing().when(serviceSpy).delete(id);

        // Then
        mockMvc.perform(delete("/parking-spot/delete/0a96e04e-b60f-4b69-9524-e221cf341ccb")
                                .contentType("application/json"))
               .andExpect(status().isNoContent());

        verify(parkingSpotService, times(1)).delete(id);
    }

    @Test
    void shouldFailDeleteWhenParkingSpotNotFound() throws Exception {
        mockMvc.perform(delete("/parking-spot/delete/0a96e04e-b60f-4b69-9524-e221cf341ccb")
                                .contentType("application/json"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Parking spot not found."));
    }
}