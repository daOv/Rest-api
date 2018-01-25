package com.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BookControllerIT {

	@LocalServerPort
	private int port;
	private TestRestTemplate template;

	@Before
	public void setUp() {
		template = new TestRestTemplate();
	}

	@Test
	public void retriveBooks() throws Exception {
		String uri = "/api/books";
		String expected = "[{\"id\":1,\"title\":\"Book\",\"description\":\"good Book\"},{\"id\":2,\"title\":\"book\","
				+ "\"description\":\"bad Book\"},{\"id\":3,\"title\":\"Book\","
				+ "\"description\":\"very good Book\"},{\"id\":4,\"title\":\"book\",\"description\":\"very bad Book\"}]";
		ResponseEntity<String> response = template.getForEntity(createUrl(uri), String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	private String createUrl(String uri) {
		return "http://localhost:" + port + uri;
	}
}
