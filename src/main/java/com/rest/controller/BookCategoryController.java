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
import com.rest.model.BookCategory;
import com.rest.service.BookCategoryService;
import com.rest.util.CustomErrorType;

@RestController
@RequestMapping(value = "/api")
public class BookCategoryController {

	@Autowired
	BookCategoryService bookCategoryService;

	@GetMapping(value = "/bookCategory")
	public ResponseEntity<List<BookCategory>> getAllBookCategories() {
		List<BookCategory> bookCategories = bookCategoryService.getAllCategories();
		if (bookCategories.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<BookCategory>>(bookCategories, HttpStatus.OK);
	}

	@GetMapping(value = "/bookCategory/{id}")
	public ResponseEntity<BookCategory> getBookCategoryById(@PathVariable("id") Integer id) {
		BookCategory bookCategory = bookCategoryService.getCategoryById(id);
		if (bookCategory == null) {
			return new ResponseEntity(new CustomErrorType("Book category with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<BookCategory>(bookCategory, HttpStatus.OK);
	}

	@PostMapping(value = "/bookCategory")
	public ResponseEntity<?> addBookCategory(@Valid @RequestBody(required = false) BookCategory bookCategory,
			UriComponentsBuilder ucBuilder) {
		if (bookCategory == null) {
			return new ResponseEntity(new CustomErrorType("Book category object must be provided."),
					HttpStatus.BAD_REQUEST);
		}
		bookCategoryService.saveCategory(bookCategory);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/bookCategory/{id}").buildAndExpand(bookCategory.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/bookCategory/{id}")
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

	@PutMapping(value = "/bookCategory/{id}")
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