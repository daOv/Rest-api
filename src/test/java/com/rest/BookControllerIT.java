package com.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.exception.ExceptionResponse;
import com.rest.utils.CustomFileReader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.rest.model.Book;
import com.rest.model.BookCategory;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.containsString;
import org.json.JSONException;
import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BookControllerIT {

	@LocalServerPort
	private int port;
	private static  TestRestTemplate template;
	private static HttpHeaders headers;
	private HttpEntity<Book> requestEntity;
	private String resurceLocation;
	private static final String LOCAL_HOST = "http://localhost:";
	private static final String BOOK_LIST_JSON_LOCATION = "json/expected_book_list.json";
	private static final String BOOK_JSON_LOCATION = "json/expected_book.json";
	private static CustomFileReader customFileReader;
	private String expected;
	private static ObjectMapper objectMapper;

	@BeforeClass
	public static void beforeClassMethod() {
		template = new TestRestTemplate();
		headers = new HttpHeaders();
		customFileReader = new CustomFileReader();
		objectMapper = new ObjectMapper();
	}

	@Before
	public void setUp() {
		requestEntity = new HttpEntity<Book>(new Book("testTitle", "testDescription", new BookCategory(1)));
	}

	@Test
	public void retriveBooks() throws Exception {
		expected = customFileReader.readFile(BOOK_LIST_JSON_LOCATION);
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books"), String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void retriveBookById() throws Exception {
		expected = customFileReader.readFile(BOOK_JSON_LOCATION);
		ResponseEntity<Book> response = template.getForEntity(createUrl("api/books/1"), Book.class);
		Book testBook = objectMapper.readValue(customFileReader.readFile(BOOK_JSON_LOCATION),Book.class);
		assertEquals(testBook.getBookCategory().getName(), response.getBody().getBookCategory().getName());
		ResponseEntity<String> responseEntity = template.getForEntity(createUrl("api/books/241"),String.class);
		assertEquals(404,responseEntity.getStatusCodeValue());
	}

	@Test
	public void createBook() {
		resurceLocation = template.postForLocation(createUrl("/api/books"), requestEntity, Book.class).toString();
		ResponseEntity<Book> getResponse = template.getForEntity(resurceLocation, Book.class);
		assertEquals("testDescription", getResponse.getBody().getDescription());
		assertThat(resurceLocation, containsString("api/books/"));
		Book badBook = new Book(null,"",new BookCategory());
		ResponseEntity<ExceptionResponse> reEn = template.postForEntity(createUrl("/api/books"),badBook,ExceptionResponse.class);
		assertEquals(reEn.getBody().getErrorCode(),"Validation Error");

	}

	@Test
	public void updateBook() throws IOException {
		resurceLocation = template.postForLocation(createUrl("/api/books"), requestEntity, Book.class).toString();
		HttpEntity<Book> putEntity = new HttpEntity<>(new Book("updatedBook","updatedDescription",new BookCategory(2)));
		template.put(resurceLocation,putEntity);
		ResponseEntity<Book> getResponseAfterUpdate = template.getForEntity(resurceLocation, Book.class);
		assertThat(getResponseAfterUpdate.getBody().getDescription(),containsString("updatedDescription"));
	}

	@Test
	public void deleteBook() throws JSONException {
		template.delete(createUrl("/api/books/2"));
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books/2"), String.class);
		expected = "{\"errorMessage\":\"book not found\"}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	private String createUrl(String uri) {
		return LOCAL_HOST + port + uri;
	}
}
