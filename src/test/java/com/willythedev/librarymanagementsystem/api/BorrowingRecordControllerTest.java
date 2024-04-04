package com.willythedev.librarymanagementsystem.api;

import com.willythedev.librarymanagementsystem.model.BookDto;
import com.willythedev.librarymanagementsystem.model.BorrowingRecordDto;
import com.willythedev.librarymanagementsystem.model.PatronDto;
import com.willythedev.librarymanagementsystem.security.TokenProvider;
import com.willythedev.librarymanagementsystem.service.BookService;
import com.willythedev.librarymanagementsystem.service.BorrowingRecordService;
import com.willythedev.librarymanagementsystem.service.PatronService;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
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

@WebMvcTest(BorrowingRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
class BorrowingRecordControllerTest {
  private static BorrowingRecordDto getBorrowingRecordDto() {
    PatronDto patronDto = new PatronDto("f8f78e70-a8cf-4766-baf9-bfcc7d671a85", "Jane Doe");
    BookDto bookDto =
        new BookDto(
            "f8f78e70-a8cf-4766-baf9-bfcc7d671a86",
            "1-4028-9462-7",
            "Sample Book",
            "Awesome Author",
            2024);
    BorrowingRecordDto borrowingRecordDto =
        new BorrowingRecordDto("f8f78e70-a8cf-4766-baf9-bfcc7d671a89", bookDto, patronDto);
    return borrowingRecordDto;
  }

  private static UniversalResponse getBorrowingUniversalResponse() {
    BorrowingRecordDto borrowingRecordDto = getBorrowingRecordDto();
    return new UniversalResponse(200, "Record saved successfully", borrowingRecordDto);
  }

  private static UniversalResponse getReturningUniversalResponse() {
    BorrowingRecordDto borrowingRecordDto = getBorrowingRecordDto();
    return new UniversalResponse(200, "Record updated successfully", borrowingRecordDto);
  }

  @MockBean BookService bookService;
  @MockBean PatronService patronService;
  @MockBean private BorrowingRecordService borrowingRecordService;
  @Autowired private MockMvc mockMvc;
  @MockBean private TokenProvider tokenProvider;

  @Test
  void canBorrowBookTest() throws Exception {
    final String bookId = "foo";
    final String patronId = "bar";
    UniversalResponse universalResponse = getBorrowingUniversalResponse();

    Mockito.when(borrowingRecordService.borrowBook(bookId, patronId)).thenReturn(universalResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/borrow/{bookId}/patron/{patronId}", bookId, patronId)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Record saved successfully"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.id")
                .value("f8f78e70-a8cf-4766-baf9-bfcc7d671a89"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.book.id")
                .value("f8f78e70-a8cf-4766-baf9-bfcc7d671a86"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.patron.id")
                .value("f8f78e70-a8cf-4766-baf9-bfcc7d671a85"));
  }

  @Test
  void returnBook() throws Exception {
    final String bookId = "foo";
    final String patronId = "bar";
    UniversalResponse universalResponse = getReturningUniversalResponse();

    Mockito.when(borrowingRecordService.returnBook(bookId, patronId)).thenReturn(universalResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(
                "/api/v1/return/{bookId}/patron/{patronId}", bookId, patronId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message").value("Record updated successfully"));
  }
}
