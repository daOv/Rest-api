package com.rest.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
@Table(name = "book_category")
public class BookCategory {
	private int id;
	private String name;
	private Set<Book> books;
	
	public BookCategory() {

	}
	
	public BookCategory(int id, String name, Set<Book> books) {
		this.id = id;
		this.name = name;
		this.books = books;
	}
	
	public BookCategory(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public BookCategory(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "bookCategory", cascade = CascadeType.ALL)
	public Set<Book> getBooks() {
		return books;
	}
														
	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	

}
