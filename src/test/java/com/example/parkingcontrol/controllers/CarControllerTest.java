package com.example.parkingcontrol.controllers;

import com.example.parkingcontrol.dtos.CarDTO;
import com.example.parkingcontrol.models.CarModel;
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

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        carDTO.setCarBrand("Audi");
        carDTO.setCarModel("A1");
        carDTO.setCarColor("Silver");
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
        carDTO.setCarBrand("Audi");
        carDTO.setCarModel("A1");
        carDTO.setCarColor("Silver");
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
}