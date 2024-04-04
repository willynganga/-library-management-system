package com.willythedev.librarymanagementsystem.api;

import com.google.gson.Gson;
import com.willythedev.librarymanagementsystem.model.BookDto;
import com.willythedev.librarymanagementsystem.security.TokenProvider;
import com.willythedev.librarymanagementsystem.service.BookService;
import com.willythedev.librarymanagementsystem.wrapper.CreateBookDto;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import com.willythedev.librarymanagementsystem.wrapper.UpdateBookDto;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {
  private static BookDto getBookDto() {
    return new BookDto(null, "1-4028-9462-7", "Sample Book", "Awesome Author", 2024);
  }

  @MockBean private BookService bookService;
  @Autowired private MockMvc mockMvc;
  @MockBean private TokenProvider tokenProvider;

  @Test
  void canAddBookTest() throws Exception {
    CreateBookDto createBookDto =
        new CreateBookDto("1-4028-9462-7", "Sample Book", "Awesome Author", 2024);
    Gson gson = new Gson();
    Mockito.when(bookService.addBook(ArgumentMatchers.any()))
        .thenReturn(new UniversalResponse(200, "Added book successfully", getBookDto()));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(createBookDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Added book successfully"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.isbn").value("1-4028-9462-7"));
  }

  @Test
  void canDeleteBookTest() throws Exception {
    final String bookId = "foo";

    Mockito.when(bookService.deleteBook(bookId))
        .thenReturn(new UniversalResponse(200, "Book deleted successfully", Map.of()));

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/api/v1/books/" + bookId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Book deleted successfully"));
  }

  @Test
  void canFindAllBooksTest() throws Exception {
    final int page = 0;
    final int size = 0;
    UniversalResponse universalResponse =
        new UniversalResponse(
            200,
            "Books List",
            Map.of("currentPage", 0, "totalPages", 1, "books", List.of(getBookDto())));

    Mockito.when(bookService.findAllBooks(page, size)).thenReturn(universalResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/books")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Books List"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.books[0].isbn").value("1-4028-9462-7"));
  }

  @Test
  void canFindBookTest() throws Exception {
    final String bookId = "foo";

    Mockito.when(bookService.findBook(bookId))
        .thenReturn(new UniversalResponse(200, "Book Found", getBookDto()));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/books/" + bookId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Book Found"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.isbn").value("1-4028-9462-7"));
  }

  @Test
  void canUpdateBookTest() throws Exception {
    final String bookId = "foo";
    UpdateBookDto updateBookDto =
        new UpdateBookDto("1-4028-9462-7", "Sample Book2", "Awesome Author", 2024);
    BookDto bookDto = new BookDto(null, "1-4028-9462-7", "Sample Book2", "Awesome Author", 2024);
    Gson gson = new Gson();
    Mockito.when(bookService.updateBook(bookId, updateBookDto))
        .thenReturn(new UniversalResponse(200, "Updated book successfully", bookDto));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/v1/books/" + bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(updateBookDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Updated book successfully"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Sample Book2"));
  }
}
