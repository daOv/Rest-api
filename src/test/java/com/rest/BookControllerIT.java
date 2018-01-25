package com.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("test.properties")
public class BookControllerIT {

	@LocalServerPort
	private int port;

	private TestRestTemplate template = new TestRestTemplate();

	@Test
	public void retriveBooks() throws Exception {
		
		String expected = "";
		String uri = "/api/books";
		ResponseEntity<String> response = template.getForEntity(createUrl(uri), String.class);

		JSONAssert.assertEquals(expected, response.getBody(),false);
	}

	private String createUrl(String uri) {
		return "http://localhost:" + port + uri;
	}
}
