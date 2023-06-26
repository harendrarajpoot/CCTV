package com.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.app.modal.Category;


public interface CategoryService {

	ResponseEntity<String> addCategory(Map<String, String> requestMap);

	ResponseEntity<List<Category>> getAllCategory(String filerValue);

	ResponseEntity<String> updateCategory(Map<String, String> requestMap);
	

}
