package com.rest;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
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
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;

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
		HttpEntity<Book> request = new HttpEntity<Book>(new Book(2, "myTitle", "myDescription", new BookCategory(1)));
		HttpEntity<Book> postResponse = template.postForEntity(createUrl("/api/books"), request, Book.class);
		ResponseEntity<Book> response = template.getForEntity(createUrl("/api/books/2"), Book.class);
		assertEquals("myDescription", response.getBody().getDescription());
		String actual = postResponse.getHeaders().getLocation().toString();
		Assert.assertThat(actual, containsString("api/books/2"));
		Assert.assertThat(actual,startsWith("http://localhost:"));


	}

	@Test
	public void deleteBook() throws JSONException {
		template.delete(createUrl("/api/books/2"));
		ResponseEntity<String> response = template.getForEntity(createUrl("api/books/2"), String.class);
		String expected = "{\"errorMessage\":\"Book with id 2 not found.\"}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void updateBook() throws JSONException {
		// template.put(createUrl("/api/book"), ne);
	}

	private String createUrl(String uri) {
		return LOCAL_HOST + port + uri;
	}
}
