package com.app.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.constants.Message;
import com.app.exception.ExceptionUtils;
import com.app.modal.Product;
import com.app.service.ProductService;
import com.app.wrapper.ProductWrapper;

@RequestMapping(path = "/product")
@RestController
public class ProductRestController {

	@Autowired
	private ProductService productService;

	@PostMapping("/add")
	public ResponseEntity<String> addNewProduct(@RequestBody(required = true) Map<String, String> requestMap) {
		try {

			return productService.addNewProduct(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/get")
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		try {
			return productService.getAllProduct();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/update")
	public ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String, String> requestMap) {
		try {

			return productService.updateProduct(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
		try {

			return productService.deleteProduct(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/updateStatus")
	public ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String, String> requestMap) {
		try {

			return productService.updateStatus(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/getByCategory/{id}")
	public ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer id) {
			try {
				return productService.getByCategory(id);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	@GetMapping("/getProductById/{id}")
	public ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id) {
			try {
				return productService.getProductById(id);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return new ResponseEntity<ProductWrapper>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
}
