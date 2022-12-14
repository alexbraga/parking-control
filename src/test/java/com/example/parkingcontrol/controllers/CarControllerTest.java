package com.example.parkingcontrol.controllers;

import com.example.parkingcontrol.dtos.CarDTO;
import com.example.parkingcontrol.models.CarModel;
import com.example.parkingcontrol.models.ParkingSpotModel;
import com.example.parkingcontrol.services.CarService;
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

@WebMvcTest(CarController.class)
@ActiveProfiles("test")
class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CarService carService;

    @Test
    void shouldSaveNewCar() throws Exception {
        // Given
        CarDTO carDTO = new CarDTO();
        carDTO.setBrand("Audi");
        carDTO.setModel("A1");
        carDTO.setColor("Silver");
        carDTO.setLicensePlate("GPK-6219");

        CarModel carModel = new CarModel();
        carModel.setId(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c"));

        BeanUtils.copyProperties(carDTO, carModel);

        // When
        Mockito.when(carService.save(any(CarModel.class))).thenReturn(carModel);

        // Then
        mockMvc.perform(post("/cars")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(carDTO)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is("3e01ec1b-85c1-4892-bf11-c02eca5b198c")));
    }

    @Test
    void shouldFailSavingWhenLicensePlateExists() throws Exception {
        // Given
        CarDTO carDTO = new CarDTO();
        carDTO.setBrand("Audi");
        carDTO.setModel("A1");
        carDTO.setColor("Silver");
        carDTO.setLicensePlate("GPK-6219");

        // When
        Mockito.when(carService.existsByLicensePlate(carDTO.getLicensePlate())).thenReturn(true);

        // Then
        mockMvc.perform(post("/cars")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(carDTO)))
               .andExpect(status().isConflict())
               .andExpect(content().string("Conflict: License plate is already in use!"));
    }

    @Test
    void shouldGetAllCars() throws Exception {
        mockMvc.perform(get("/cars")).andExpect(status().isOk());
    }

    @Test
    void shouldFindCarById() throws Exception {
        // Given
        CarModel carModel = new CarModel();
        carModel.setId(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c"));

        // When
        Mockito.when(carService.findById(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c"))).thenReturn(
                Optional.of(carModel));

        // Then
        mockMvc.perform(get("/cars/3e01ec1b-85c1-4892-bf11-c02eca5b198c")).andExpect(status().isOk());
    }

    @Test
    void shouldFailWhenCarNotFoundById() throws Exception {
        // When
        Mockito.when(carService.findById(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c")))
               .thenReturn(Optional.empty());

        // Then
        mockMvc.perform(get("/cars/3e01ec1b-85c1-4892-bf11-c02eca5b198c"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Car not found."));
    }

    @Test
    void shouldFindCarByLicensePlate() throws Exception {
        // Given
        CarModel carModel = new CarModel();
        carModel.setLicensePlate("GPK-6219");

        // When
        Mockito.when(carService.findByLicensePlate("GPK-6219")).thenReturn(Optional.of(carModel));

        // Then
        mockMvc.perform(get("/cars/license-plate?number=GPK-6219")).andExpect(status().isOk());
    }

    @Test
    void shouldFailWhenCarNotFoundByLicensePlate() throws Exception {
        // When
        Mockito.when(carService.findByLicensePlate("GPK-6219")).thenReturn(Optional.empty());

        // Then
        mockMvc.perform(get("/cars/license-plate?number=GPK-6219"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Car not found."));
    }

    @Test
    void shouldUpdateCar() throws Exception {
        // Given
        CarDTO carDTO = new CarDTO();
        carDTO.setBrand("Audi");
        carDTO.setModel("A1");
        carDTO.setColor("Silver");
        carDTO.setLicensePlate("GPK-6219");

        CarModel carModel = new CarModel();
        carModel.setId(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c"));

        // When
        Mockito.when(carService.findById(UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c")))
               .thenReturn(Optional.of(carModel));

        Mockito.when(carService.save(carModel)).thenReturn(carModel);

        BeanUtils.copyProperties(carDTO, carModel);

        // Then
        mockMvc.perform(put("/cars/update/3e01ec1b-85c1-4892-bf11-c02eca5b198c")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(carDTO)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is("3e01ec1b-85c1-4892-bf11-c02eca5b198c")))
               .andExpect(content().string(objectMapper.writeValueAsString(carModel)));
    }

    @Test
    void shouldFailUpdateWhenInvalidCarDTOSubmitted() throws Exception {
        // Given
        CarDTO carDTO = new CarDTO();
        carDTO.setBrand("");
        carDTO.setModel("A1");
        carDTO.setColor("Silver");
        carDTO.setLicensePlate("GPK-6219");

        // Then
        mockMvc.perform(put("/cars/update/3e01ec1b-85c1-4892-bf11-c02eca5b198c")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(carDTO)))
               .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailUpdateWhenCarNotFound() throws Exception {
        // Given
        CarDTO carDTO = new CarDTO();
        carDTO.setBrand("Audi");
        carDTO.setModel("A1");
        carDTO.setColor("Silver");
        carDTO.setLicensePlate("GPK-6219");

        // Then
        mockMvc.perform(put("/cars/update/3e01ec1b-85c1-4892-bf11-c02eca5b198c")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(carDTO)))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Car not found."));
    }

    @Test
    void shouldDeleteCar() throws Exception {
        // Given
        UUID id = UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c");
        CarModel carModel = new CarModel();
        carModel.setId(id);

        CarService serviceSpy = Mockito.spy(carService);

        // When
        Mockito.when(carService.findById(id)).thenReturn(Optional.of(carModel));
        Mockito.doNothing().when(serviceSpy).delete(id);

        // Then
        mockMvc.perform(delete("/cars/delete/3e01ec1b-85c1-4892-bf11-c02eca5b198c")
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string("Car deleted successfully."));

        verify(carService, times(1)).delete(id);
    }

    @Test
    void shouldRemoveFromParentAndDeleteCar() throws Exception {
        // Given
        UUID id = UUID.fromString("3e01ec1b-85c1-4892-bf11-c02eca5b198c");
        CarModel carModel = new CarModel();
        carModel.setId(id);

        ParkingSpotModel parkingSpot = new ParkingSpotModel();
        parkingSpot.setCar(carModel);

        CarService serviceSpy = Mockito.spy(carService);

        // When
        Mockito.when(carService.findById(id)).thenReturn(Optional.of(carModel));
        Mockito.doNothing().when(serviceSpy).delete(id);

        // Then
        mockMvc.perform(delete("/cars/delete/3e01ec1b-85c1-4892-bf11-c02eca5b198c")
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string("Car deleted successfully."));

        verify(carService, times(1)).delete(id);
    }

    @Test
    void shouldFailDeleteWhenCarNotFound() throws Exception {
        mockMvc.perform(delete("/cars/delete/3e01ec1b-85c1-4892-bf11-c02eca5b198c")
                               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Car not found."));
    }
}