package com.rest.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rest.model.Book;
import com.rest.repositroy.BookRepository;

@Service
public class BookService {

	@Autowired
	BookRepository bookRepository;

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book getBookById(Integer id) {
		return bookRepository.findOne(id);
	}

	public void saveBook(Book book) {
		bookRepository.save(book);
	}

	public void deleteBook(Integer id) {
		bookRepository.delete(id);
	}
}
