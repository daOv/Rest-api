package com.rest.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
