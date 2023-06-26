package com.app.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.constants.Message;
import com.app.exception.ExceptionUtils;
import com.app.modal.Category;
import com.app.service.CategoryService;

@RequestMapping(path = "/category")
@RestController
public class CategoryRestController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/add")
	public ResponseEntity<String> addCategory(@RequestBody Map<String, String> requestMap) {
		try {
			return categoryService.addCategory(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	

	@GetMapping("/get")
	public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filerValue) {
		try {
			return categoryService.getAllCategory(filerValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@PostMapping("/update")
	public ResponseEntity<String> updateCategory(@RequestBody(required=true) Map<String, String> requestMap) {
		try {
			return categoryService.updateCategory(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
