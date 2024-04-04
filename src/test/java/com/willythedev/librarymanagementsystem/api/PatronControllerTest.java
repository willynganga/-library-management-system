package com.willythedev.librarymanagementsystem.api;

import com.google.gson.Gson;
import com.willythedev.librarymanagementsystem.model.PatronDto;
import com.willythedev.librarymanagementsystem.security.TokenProvider;
import com.willythedev.librarymanagementsystem.service.PatronService;
import com.willythedev.librarymanagementsystem.wrapper.CreatePatronDto;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import com.willythedev.librarymanagementsystem.wrapper.UpdatePatronDto;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(PatronController.class)
@AutoConfigureMockMvc(addFilters = false)
class PatronControllerTest {
  private static PatronDto getPatronDto() {
    return new PatronDto("f8f78e70-a8cf-4766-baf9-bfcc7d671a85", "Jane Doe");
  }

  @MockBean PatronService patronService;
  @Autowired private MockMvc mockMvc;
  @MockBean private TokenProvider tokenProvider;

  @Test
  void canAddPatronTest() throws Exception {
    Gson gson = new Gson();
    CreatePatronDto createPatronDto =
        new CreatePatronDto("Jane Doe", "Street 171", "test@test.com", "254712345678");
    PatronDto patronDto = getPatronDto();
    UniversalResponse universalResponse =
        new UniversalResponse(200, "Patron added successfully", patronDto);

    Mockito.when(patronService.addPatron(createPatronDto)).thenReturn(universalResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(createPatronDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Patron added successfully"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.id")
                .value("f8f78e70-a8cf-4766-baf9-bfcc7d671a85"));
  }

  @Test
  void canDeletePatronTest() throws Exception {
    final String patronId = "foo";

    Mockito.when(patronService.deletePatron(patronId))
        .thenReturn(new UniversalResponse(200, "Patron deleted successfully", Map.of()));

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/api/v1/patrons/" + patronId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message").value("Patron deleted successfully"));
  }

  @Test
  void canFindAllPatronsTest() throws Exception {
    final int page = 0;
    final int size = 0;
    UniversalResponse universalResponse =
        new UniversalResponse(
            200,
            "Patrons List",
            Map.of("currentPage", 0, "totalPages", 1, "patrons", List.of(getPatronDto())));

    Mockito.when(patronService.findAllPatrons(page, size)).thenReturn(universalResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/patrons")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Patrons List"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.patrons[0].id")
                .value("f8f78e70-a8cf-4766-baf9-bfcc7d671a85"));
  }

  @Test
  void canFindPatronTest() throws Exception {
    final String patronId = "foo";

    Mockito.when(patronService.findPatron(patronId))
        .thenReturn(new UniversalResponse(200, "Patron Found", getPatronDto()));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/patrons/" + patronId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Patron Found"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.id")
                .value("f8f78e70-a8cf-4766-baf9-bfcc7d671a85"));
  }

  @Test
  void canUpdatePatronTest() throws Exception {
    final String patronId = "foo";
    UpdatePatronDto updatePatronDto =
        new UpdatePatronDto("John Doe", "Street 171", "test@test.com", "254712345678");
    PatronDto patronDto = new PatronDto("f8f78e70-a8cf-4766-baf9-bfcc7d671a85", "John Doe");
    Gson gson = new Gson();
    Mockito.when(patronService.updatePatron(patronId, updatePatronDto))
        .thenReturn(new UniversalResponse(200, "Updated patron successfully", patronDto));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/v1/patrons/" + patronId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(updatePatronDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Updated patron successfully"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("John Doe"));
  }
}
