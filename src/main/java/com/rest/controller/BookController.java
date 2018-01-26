package com.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> getAllCategories() {
		List<Book> books = bookService.getAllBooks();
		if (books.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

	@RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
	public ResponseEntity<Book> getBookById(@PathVariable("id") Integer id) {
		Book book = bookService.getBookById(id);
		if (book == null) {
			return new ResponseEntity(new CustomErrorType("Book with id " + id + " not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Book>(book, HttpStatus.OK);
	}

	@RequestMapping(value = "/books", method = RequestMethod.POST)
	public ResponseEntity<?> addBook(@RequestBody Book book, UriComponentsBuilder ucBuilder) {
		bookService.saveBook(book);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/books/{id}").buildAndExpand(book.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/books/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBook(@PathVariable("id") Integer id) {
		Book book = bookService.getBookById(id);
		if (book == null) {
			return new ResponseEntity(new CustomErrorType("Unable to delete. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		bookService.deleteBook(id);
		return new ResponseEntity<BookCategory>(HttpStatus.NO_CONTENT);
	}
	@RequestMapping(value = "/books/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateBook(@RequestBody Book book, @PathVariable("id") Integer id) {
		Book currentBook = bookService.getBookById(id);
		if (currentBook == null) {
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		currentBook.setDescription(book.getDescription());
		currentBook.setTitle(book.getTitle());
		currentBook.setBookCategory(book.getBookCategory());
		return new ResponseEntity<Book>(currentBook, HttpStatus.OK);
	}
	
}
