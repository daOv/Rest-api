package com.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
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

	@Autowired
	private MockMvc mvc;

	List<Book> mockBookList;

	@Before
	public void setUp() {
		mockBookList = Arrays.asList(new Book(1, "Book", "good Book"), new Book(2, "Book2", "bad Book"));
	}

	@Test
	public void getAllBooks() throws Exception {
		when(bookService.getAllBooks()).thenReturn(mockBookList);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/books").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String expected = "[{'id':1,'title':'Book','description':'good Book'},{'id':2,'title':Book2,'description':'bad Book'}]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getOneBook() throws Exception {
		when(bookService.getBookById(1)).thenReturn(mockBookList.get(0));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/books/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String expected = "{'id':1,'title':'Book','description':'good Book'}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
}