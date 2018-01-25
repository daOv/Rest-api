package com.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rest.model.Book;
import com.rest.service.BookService;


@RestController
@RequestMapping("/api")
public class BookController {
	@Autowired
	BookService bookService;

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public List<Book> getAllBooks() {
		return bookService.getAllBooks();
	}

	@RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
	public Book getBookCategoryById(@PathVariable("id") Integer id) {
		return bookService.getBookById(id);
	}

	@RequestMapping(value = "/books", method = RequestMethod.POST)
	public void addBookCategory(@RequestBody Book book) {
		bookService.saveBook(book);
	}

	@RequestMapping(value = "/books/{id}", method = RequestMethod.DELETE)
	public void deletBookCategory(@PathVariable("id") Integer id) {
		bookService.deleteBook(id);
	}

	@RequestMapping(value = "/books/{id}", method = RequestMethod.PUT)
	public void updateBookCategory(@RequestBody Book bookDetails, @PathVariable("id") Integer id) {
		Book book = bookService.getBookById(id);
		book.setTitle(bookDetails.getTitle());
		book.setDescription(bookDetails.getDescription());
		book.setBookCategory(book.getBookCategory());
		bookService.saveBook(book);
	}
}
