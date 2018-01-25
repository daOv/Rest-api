package com.rest.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@ConfigurationProperties("app")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String title;
	private String description;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "book_category_id")
	private BookCategory bookCategory;

	public Book() {

	}

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

	@Override
	public String toString() {
		String result = String.format("Book[id=%d, title='%s',description='%s',bookCategory='%s']%n", id, title,
				description, getBookCategory());
		return result;
	}
	}
