package com.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.constants.Message;
import com.app.exception.ExceptionUtils;
import com.app.jwt.JwtFilter;
import com.app.modal.Category;
import com.app.modal.Product;
import com.app.repository.ProductRepository;
import com.app.service.ProductService;
import com.app.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {

			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap, false)) {
					productRepository.save(getProductFromMap(requestMap, false));
					return ExceptionUtils.getReponseEntity("Product " + Message.SUCCESS_REGISTERED, HttpStatus.OK);
				}

				// if does not pass any value return invalid error
				return ExceptionUtils.getReponseEntity(Message.INVALID_DATA, HttpStatus.BAD_REQUEST);

			} else {
				return ExceptionUtils.getReponseEntity(Message.UNATHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {

		Product product = new Product();
		Category category = new Category();

		category.setId(Integer.parseInt(requestMap.get("categoryId")));

		if (isAdd) {
			product.setId(Integer.parseInt(requestMap.get("id")));
		} else {
			product.setStatus("true");
		}
		product.setName(requestMap.get("name"));
		product.setPrice(Integer.parseInt(requestMap.get("price")));
		product.setDescription(requestMap.get("description"));
		product.setCategory(category);

		return product;
	}

	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name") && requestMap.containsKey("price") && requestMap.containsKey("description")
				&& requestMap.containsKey("categoryId")) {
			if (requestMap.containsKey("name") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}

		}
		return false;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		try {
			return new ResponseEntity<List<ProductWrapper>>(productRepository.getAllProduct(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {

		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap, true)) {

					Optional optional = productRepository.findById(Integer.parseInt(requestMap.get("id")));

					if (optional != null && optional.empty() != optional) {
						Product product = getProductFromMap(requestMap, true);
						product.setStatus("true");
						productRepository.save(product);
						return ExceptionUtils.getReponseEntity("Product " + Message.UPDATED, HttpStatus.OK);
					} else {
						return ExceptionUtils.getReponseEntity("Product " + Message.NOT_FOUND, HttpStatus.OK);

					}

				}

				// if does not pass any value return invalid error
				return ExceptionUtils.getReponseEntity(Message.INVALID_DATA, HttpStatus.BAD_REQUEST);

			} else {
				return ExceptionUtils.getReponseEntity(Message.UNATHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional optional = productRepository.findById(id);
				if (optional != null && optional.empty() != optional) {
					productRepository.deleteById(id);
					return ExceptionUtils.getReponseEntity("Product " + Message.DELETED, HttpStatus.OK);
				} else {
					return ExceptionUtils.getReponseEntity("Product " + Message.NOT_FOUND, HttpStatus.OK);

				}

			} else {
				return ExceptionUtils.getReponseEntity(Message.UNATHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {

				Optional optional = productRepository.findById(Integer.parseInt(requestMap.get("id")));

				if (optional != null && optional.empty() != optional) {
					productRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					return ExceptionUtils.getReponseEntity("Product status " + Message.UPDATED, HttpStatus.OK);
				} else {
					return ExceptionUtils.getReponseEntity("Product " + Message.NOT_FOUND, HttpStatus.OK);

				}

			}

			else {
				return ExceptionUtils.getReponseEntity(Message.UNATHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {

		try {

			List<ProductWrapper> category = productRepository.getProductByCategory(id);
			if (category.size() > 0) {
				return new ResponseEntity<>(category, HttpStatus.OK);
			} else {

				return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.OK);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity getProductById(Integer id) {
		try {

			List<ProductWrapper> list = productRepository.getProductById(id);
			
			if(list.size()>0)
			{return new ResponseEntity<List<ProductWrapper>>( list, HttpStatus.OK);
			}
			else
			{
				
				return new ResponseEntity<List<ProductWrapper>>( list, HttpStatus.OK);
			}
		
			

			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	
	}

}
