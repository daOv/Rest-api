package com.rest.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.rest.model.Book;
import com.rest.model.BookCategory;
import com.rest.service.BookService;
import com.rest.util.CustomErrorType;

@RestController
@RequestMapping("/api")
public class BookController {

	@Autowired
	BookService bookService;

	@GetMapping(value = "/books")
	public ResponseEntity<List<Book>> getAllCategories() {
		List<Book> books = bookService.getAllBooks();
		if (books.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

	@GetMapping(value = "/books/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable("id") Integer id) {
		Book book = bookService.getBookById(id);
		if (book == null) {
			return new ResponseEntity(new CustomErrorType("Book with id " + id + " not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Book>(book, HttpStatus.OK);
	}

	@PostMapping(value = "/books")
	public ResponseEntity<?> addBook(@Valid @RequestBody(required = false) Book book, UriComponentsBuilder ucBuilder) {
		if (book == null) {
			return new ResponseEntity(new CustomErrorType("Book object must be provided."), HttpStatus.BAD_REQUEST);
		}
		bookService.saveBook(book);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/books/{id}").buildAndExpand(book.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/books/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable("id") Integer id) {
		Book book = bookService.getBookById(id);
		if (book == null) {
			return new ResponseEntity(new CustomErrorType("Unable to delete. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		bookService.deleteBook(id);
		return new ResponseEntity<BookCategory>(HttpStatus.NO_CONTENT);
	}

	@PutMapping(value = "/books/{id}")
	public ResponseEntity<?> updateBook(@Valid @RequestBody(required = false) Book book,
			@PathVariable("id") Integer id) {
		Book currentBook = bookService.getBookById(id);
		if (currentBook == null) {
			return new ResponseEntity(new CustomErrorType("Unable to upate. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		if (book.getId() != 0) {
			currentBook.setId(id);
		}
		if (book.getDescription() != null) {
			currentBook.setDescription(book.getDescription());
		}
		if (book.getTitle() != null) {
			currentBook.setTitle(book.getTitle());
		}
		if (!currentBook.getBookCategory().equals(null)) {
			currentBook.setBookCategory(book.getBookCategory());
		}

		bookService.saveBook(currentBook);
		return new ResponseEntity<Book>(currentBook, HttpStatus.OK);
	}
}
