package com.rest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.model.BookCategory;
import com.rest.repositroy.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.rest.controller.BookController;
import com.rest.model.Book;
import com.rest.service.BookService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
@ContextConfiguration(classes = Application.class)
public class BookControllerTest {

	@MockBean
	private BookService bookService;

	@MockBean
	private BookRepository bookRepository;

	@Autowired
	private MockMvc mvc;

	List<Book> mockBookList;
	Book mockBook;
	List <Book> mockEmptyBookList;

	@Before
	public void setUp() {
		mockBookList = Arrays.asList(new Book(1, "Book", "good Book"), new Book(2, "Book2", "bad Book"));
		mockBook = new Book("testBook","testDescription", new BookCategory(1));
		mockEmptyBookList = new ArrayList<>();

	}

	@Test
	public void getAllBooks() throws Exception {
		when(bookService.getAllBooks()).thenReturn(mockBookList);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/books").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String expected = "[{'id':1,'title':'Book','description':'good Book'},{'id':2,'title':Book2,'description':'bad Book'}]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
		when(bookService.getAllBooks()).thenReturn(mockEmptyBookList);
		mvc.perform(MockMvcRequestBuilders.get("/api/books").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()).andReturn();
	}

	@Test
	public void getOneBook_success() throws Exception {
		when(bookService.getBookById(1)).thenReturn(mockBookList.get(0));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/books/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String expected = "{'id':1,'title':'Book','description':'good Book'}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getOneBook_fail_404() throws Exception {
		when(bookService.getBookById(1)).thenReturn(null);
		mvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deleteBook_success() throws Exception{
		doNothing().when(bookService).deleteBook(1);
		mvc.perform(MockMvcRequestBuilders.delete("/api/books/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void deleteBook_fail_400() throws Exception{
		when(bookService.getBookById(3)).thenReturn(null);
		mvc.perform(
				MockMvcRequestBuilders.delete("/users/{id}", 2))
				.andExpect(status().isNotFound());
	}

	@Test
	public void addBook() throws Exception{
		doNothing().when(bookService).saveBook(mockBook);
		mvc.perform(MockMvcRequestBuilders.post("/api/books").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(mockBook)))
				.andExpect(status().isCreated());
	}

	@Test
	public void updateBook_success() throws Exception{
		Book book = new Book(1,"updatedBook","updatedDescription", new BookCategory(1));
		when(bookService.getBookById(book.getId())).thenReturn(book);
		doNothing().when(bookService).saveBook(book);
		mvc.perform(MockMvcRequestBuilders.put("/api/books/1").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(book)))
				.andExpect(status().isOk());
	}

	@Test
	public void updateBook_fail_400() throws Exception{
		when(bookService.getBookById(2)).thenReturn(null);
		mvc.perform(
				MockMvcRequestBuilders.put("/api/books/{id}", 2)
						.contentType(MediaType.APPLICATION_JSON)).
				andExpect(status().isBadRequest());
	}


	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}