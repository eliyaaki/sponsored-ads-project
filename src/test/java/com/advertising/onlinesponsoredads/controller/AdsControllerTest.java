package com.advertising.onlinesponsoredads.controller;

import com.advertising.onlinesponsoredads.contoller.AdsController;
import com.advertising.onlinesponsoredads.dto.request.CreateCampaignRequestDTO;
import com.advertising.onlinesponsoredads.dto.response.CreateCampaignResponseDTO;
import com.advertising.onlinesponsoredads.dto.response.ServeAdResponseDTO;
import com.advertising.onlinesponsoredads.exception.BadRequestException;
import com.advertising.onlinesponsoredads.exception.NotFoundException;
import com.advertising.onlinesponsoredads.service.AdsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(MockitoExtension.class)
class AdsControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AdsService adsService;

    @InjectMocks
    private AdsController adsController;

    @BeforeEach
    void setUp() {
        // Set up the request context
        mockMvc = MockMvcBuilders.standaloneSetup(adsController).build();
    }


    @Test
    void testCreateCampaign_Success() throws Exception {
        // Mock data
        CreateCampaignRequestDTO request = CreateCampaignRequestDTO.builder()
                .name("campaign1")
                .bid(100.0)
                .startDate(LocalDate.now())
                .productSerialNumbers(List.of("ETE53245"))
                .build();
        CreateCampaignResponseDTO response = CreateCampaignResponseDTO.builder()
                .id(42235353L)
                .name("campaign1")
                .bid(100)
                .startDate(LocalDate.now())
                .build();

        // Mock service method
        when(adsService.createCampaign(request)).thenReturn(response);

        // Perform the request using MockMvc
        mockMvc.perform(MockMvcRequestBuilders.post("/api/ads/createCampaign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name").value("campaign1")); // Adjust as needed

        // Verify that the service method was called
        verify(adsService, times(1)).createCampaign(request);
    }

    @Test
    void testCreateCampaign_Failure() throws Exception {
        // Mock data
        CreateCampaignRequestDTO request = CreateCampaignRequestDTO.builder()
                .name("campaign1")
                .bid(100.0)
                .startDate(LocalDate.now())
                .productSerialNumbers(List.of("ETE53245"))
                .build();

        // Mock service method to throw a BadRequestException
        when(adsService.createCampaign(request)).thenThrow(new BadRequestException("Invalid campaign request"));


        // Act and Assert
        Exception exception = assertThrows(BadRequestException.class, () -> {
            adsController.createCampaign(request);
        });

        // Assert specific exception details if needed
        assertEquals("Invalid campaign request", exception.getMessage());

        // Verify that the service method was called
        verify(adsService, times(1)).createCampaign(request);
    }

    @Test
    void testCreateCampaign_ValidationFailure() throws Exception {
        // Mock data with invalid request
        CreateCampaignRequestDTO request = new CreateCampaignRequestDTO();
        mockMvc.perform(post("/api/ads/createCampaign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));
        // Verify that the service method was called
        verify(adsService, times(0)).createCampaign(request);
    }

    @Test
    void testServeAd_Success() throws Exception {
        // Mock data
        String category = "TestCategory";
        ServeAdResponseDTO response = ServeAdResponseDTO.builder()
                .id(412L)
                .price(500)
                .serialNumber("hjhhg")
                .build();

        // Mock service method
        when(adsService.getAdByCategory(category)).thenReturn(response);

        // Perform the request using MockMvc
        mockMvc.perform(get("/api/ads/serveAd")
                        .param("category", category))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.price").value(500));

        // Verify that the service method was called
        verify(adsService, times(1)).getAdByCategory(category);
    }

    @Test
    void testServeAd_NotFound() throws Exception {
        // Mock data with a category that does not exist
        String category = "NonExistentCategory";

        // Mock service method to throw NotFoundException
        when(adsService.getAdByCategory(category)).thenThrow(new NotFoundException("Category not found"));


        // Act and Assert
        Exception exception = assertThrows(NotFoundException.class, () -> {
            adsController.serveAd(category);
        });

        // Assert specific exception details if needed
        assertEquals("Category not found", exception.getMessage());

        // Verify that the service method was called
        verify(adsService, times(1)).getAdByCategory(category);
    }

    // Helper method to convert objects to JSON string
    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = JsonMapper.builder()
                    .findAndAddModules()
                    .build();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
