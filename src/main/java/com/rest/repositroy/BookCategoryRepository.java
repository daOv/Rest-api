package com.rest.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rest.model.BookCategory;


public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer>{
}