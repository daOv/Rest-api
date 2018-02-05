package com.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "book_id")
	private int id;
	@NotNull(message = "title can not be null.")
	private String title;
	@NotNull(message = "description can not be null.")
	private String description;
	@NotNull(message = "category can not be null.")
	@ManyToOne
	@JoinColumn(name = "category_id_fk", referencedColumnName = "category_id")
	private BookCategory bookCategory;

	public Book() {}

	public Book(int id, String title, String description, BookCategory bookCategory) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.bookCategory = bookCategory;
	}

	public Book(int id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}
	
	public Book( String title, String description, BookCategory bookCategory) {
		this.title = title;
		this.description = description;
		this.bookCategory = bookCategory;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BookCategory getBookCategory() {
		return bookCategory;
	}

	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}

}
