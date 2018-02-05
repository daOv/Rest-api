package com.rest.service;

import java.util.List;

import com.rest.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rest.model.Book;
import com.rest.repositroy.BookRepository;

@Service
public class BookService {

	@Autowired
	BookRepository bookRepository;
	Book book;

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book getBookById(Integer id) {
		book= bookRepository.findOne(id);
		if(book==null){throw new ResourceNotFoundException(id,"book not found");}
		return book;
	}

	public void saveBook(Book book) {
		bookRepository.save(book);
	}

	public void deleteBook(Integer id) {
		book= bookRepository.findOne(id);
		if(book==null){throw new ResourceNotFoundException(id,"book not found");}
		bookRepository.delete(id);
	}
}
