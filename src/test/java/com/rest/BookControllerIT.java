package com.rest;

import static org.junit.Assert.assertTrue;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.rest.model.Book;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BookControllerIT {
	private TestRestTemplate template;

	@LocalServerPort
	private int port;

	private static final String LOCAL_HOST = "http://localhost:";

	HttpHeaders headers = new HttpHeaders();

	@Before
	public void setUp() {
		template = new TestRestTemplate();
	}

	@Test
	public void retriveBooks() throws Exception {
		String expected = "[{\"id\":1,\"title\":\"Book\",\"description\":\"good Book\"},{\"id\":2,\"title\":\"book\","
				+ "\"description\":\"bad Book\"},{\"id\":3,\"title\":\"Book\","
				+ "\"description\":\"very good Book\"},{\"id\":4,\"title\":\"book\",\"description\":\"very bad Book\"}]";
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books"), String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void retriveBookById() throws Exception {
		String expected = "{\"id\":1,\"title\":\"Book\",\"description\":\"good Book\"}";
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books/1"), String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void postBook() {
		Book book = new Book(5, "Test book", "test");
		HttpEntity<Book> entity = new HttpEntity<Book>(book, headers);
		ResponseEntity<String> response = template.exchange(createUrl("/api/books"), HttpMethod.POST, entity,
				String.class);
		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		assertTrue(actual.contains("api/books/5"));
	}

	@Test
	public void deleteBook() throws JSONException {
		template.delete(createUrl("/api/books/5"));
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books/5"), String.class);
		String expected = "{\"errorMessage\":\"Book with id 5 not found.\"}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void updateBook() throws JSONException {
		Book book = new Book(1, "Test book", "test");
		template.put(createUrl("api/books/2"), book);
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books/2"), String.class);
		String expected = "	{\"id\": 2,\"title\": \"Test book\",\"description\": \"test\"}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	private String createUrl(String uri) {
		return LOCAL_HOST + port + uri;
	}
}
