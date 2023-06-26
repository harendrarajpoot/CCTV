package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.modal.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	@Query("select c from Category c where c.id in(select p from Product p where p.status='true') ")
	 List<Category>getAllCategory();
}
