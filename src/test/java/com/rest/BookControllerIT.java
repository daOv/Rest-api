package com.rest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.rest.model.Book;
import com.rest.model.BookCategory;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import org.json.JSONException;

import java.net.URI;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BookControllerIT {

	private static  TestRestTemplate template;

	@LocalServerPort
	private int port;

	private static final String LOCAL_HOST = "http://localhost:";

	static HttpHeaders headers;
	HttpEntity<Book> requestEntity;

	@BeforeClass
	public static void beforeClassMethod() {
		template = new TestRestTemplate();
		headers = new HttpHeaders();
	}

	@Before
	public void setUp() {

	}

	@Test
	public void retriveBooks() throws Exception {
		String expected = "[{\"id\": 1,\"title\": \"testBook\",\"description\": \"test book \",\"bookCategory\": {\"id\": 1,\"name\": \"testCategory\"}}]";
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books"), String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void retriveBookById() throws Exception {
		String expected = "{\"id\": 1,\"title\": \"testBook\",\"description\": \"test book \",\"bookCategory\": {\"id\": 1,\"name\": \"testCategory\"}}";
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books/1"), String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void createBook() {
		requestEntity = new HttpEntity<Book>(new Book(2, "myTitle", "myDescription", new BookCategory(1)));
		HttpEntity<Book> postResponse = template.postForEntity(createUrl("/api/books"), requestEntity, Book.class);
		ResponseEntity<Book> getResponse = template.getForEntity(createUrl("/api/books/2"), Book.class);
		assertEquals("myDescription", getResponse.getBody().getDescription());
		String actual = postResponse.getHeaders().getLocation().toString();
		assertThat(actual, containsString("api/books/2"));
		assertThat(actual,startsWith("http://localhost:"));
	}

	@Test
	public void updateBook()  {
		requestEntity = new HttpEntity<Book>(new Book("TestBook", "Book for testing", new BookCategory(1)));
		URI postResponse = template.postForLocation(createUrl("/api/books"), requestEntity, Book.class);
		ResponseEntity<Book> getResponse = template.getForEntity(postResponse, Book.class);
		HttpEntity<Book> putEntity = new HttpEntity<>(new Book("updatedBook","updatedDescription",new BookCategory(2)));
		template.put(postResponse.toString(),putEntity);
		ResponseEntity<Book> getResponseAfterUpdate = template.getForEntity(postResponse, Book.class);
		assertThat(getResponseAfterUpdate.getBody().getDescription(),containsString("updatedDescription"));
	}

	@Test
	public void deleteBook() throws JSONException {
		template.delete(createUrl("/api/books/2"));
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books/2"), String.class);
		String expected = "{\"errorMessage\":\"Book with id 2 not found.\"}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}



	private String createUrl(String uri) {
		return LOCAL_HOST + port + uri;
	}
}
