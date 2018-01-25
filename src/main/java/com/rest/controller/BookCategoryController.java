package com.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rest.model.BookCategory;
import com.rest.service.BookCategoryService;


@RestController
@RequestMapping(value = "/api")
public class BookCategoryController {
	@Autowired
	BookCategoryService bookCategoryService;

	@RequestMapping(value = "/bookCategory", method = RequestMethod.GET)
	public List<BookCategory> getAllCategories() {
		return bookCategoryService.getAllCategories();
	}

	@RequestMapping(value = "/bookCategory/{id}", method = RequestMethod.GET)
	public BookCategory getBookCategoryById(@PathVariable("id") Integer id) {
		return bookCategoryService.getCategoryById(id);
	}

	@RequestMapping(value = "/bookCategory", method = RequestMethod.POST)
	public void addBookCategory(@RequestBody BookCategory category) {
		bookCategoryService.saveCategory(category);
	}

	@RequestMapping(value = "/bookCategory/{id}", method = RequestMethod.DELETE)
	public void deletBookCategory(@PathVariable("id") Integer id) {
		bookCategoryService.deleteCategory(id);
	}

	@RequestMapping(value = "/bookCategory/{id}", method = RequestMethod.PUT)
	public void updateBook(@RequestBody BookCategory bookCategoryDetails, @PathVariable("id") Integer id) {
		BookCategory category = bookCategoryService.getCategoryById(id);
		category.setName(bookCategoryDetails.getName());

		bookCategoryService.saveCategory(category);
	}
}