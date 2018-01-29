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
import com.rest.model.BookCategory;
import com.rest.service.BookCategoryService;
import com.rest.util.CustomErrorType;

@RestController
@RequestMapping(value = "/api")
public class BookCategoryController {

	@Autowired
	BookCategoryService bookCategoryService;

	@RequestMapping(value = "/bookCategory", method = RequestMethod.GET)
	public ResponseEntity<List<BookCategory>> getAllBookCategories() {
		List<BookCategory> bookCategories = bookCategoryService.getAllCategories();
		if (bookCategories.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<BookCategory>>(bookCategories, HttpStatus.OK);
	}

	@RequestMapping(value = "/bookCategory/{id}", method = RequestMethod.GET)
	public ResponseEntity<BookCategory> getBookCategoryById(@PathVariable("id") Integer id) {
		BookCategory bookCategory = bookCategoryService.getCategoryById(id);
		if (bookCategory == null) {
			return new ResponseEntity(new CustomErrorType("Book category with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<BookCategory>(bookCategory, HttpStatus.OK);
	}

	@RequestMapping(value = "/bookCategory", method = RequestMethod.POST)
	public ResponseEntity<?> addBookCategory(@RequestBody BookCategory category, UriComponentsBuilder ucBuilder) {
		bookCategoryService.saveCategory(category);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/bookCategory/{id}").buildAndExpand(category.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/bookCategory/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBookCategory(@PathVariable("id") Integer id) {
		BookCategory category = bookCategoryService.getCategoryById(id);
		if (category == null) {
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. Book category with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		bookCategoryService.deleteCategory(id);
		return new ResponseEntity<BookCategory>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/bookCategory/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateBookCategory(@RequestBody BookCategory bookCategoryDetails,
			@PathVariable("id") Integer id) {
		BookCategory category = bookCategoryService.getCategoryById(id);
		if (category == null) {
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. Book category with id \" + id + \" not found."),
					HttpStatus.NOT_FOUND);
		}
		category.setName(bookCategoryDetails.getName());
		bookCategoryService.saveCategory(category);
		return new ResponseEntity<BookCategory>(category, HttpStatus.OK);
	}
}