package com.rest.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.model.BookCategory;
import com.rest.repositroy.BookCategoryRepository;

@Service
public class BookCategoryService {

	@Autowired
	BookCategoryRepository bookCategoryRepository;

	public List<BookCategory> getAllCategories() {
		return bookCategoryRepository.findAll();
	}

	public BookCategory getCategoryById(Integer id) {
		return bookCategoryRepository.findOne(id);
	}

	public void saveCategory(BookCategory category) {
		bookCategoryRepository.save(category);
	}

	public void deleteCategory(Integer id) {
		bookCategoryRepository.delete(id);
	}
}
