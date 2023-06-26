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
import com.app.repository.CategoryRepository;
import com.app.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
		
		try
		{
			if(jwtFilter.isAdmin())
			{
				if(validateCategoryMap(requestMap,false))
				{
					categoryRepository.save(getCategoryFromMap(requestMap, false));
					return ExceptionUtils.getReponseEntity("Category "+Message.SUCCESS_REGISTERED, HttpStatus.OK);
				}
			}
			else
			{
				return ExceptionUtils.getReponseEntity(Message.UNATHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		}
		catch (Exception e) {
		log.error("CategoryServiceImpl: addCategory {}",e);
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {

		if (requestMap.containsKey("name")) {
			// update category
			if (requestMap.containsKey("name") && validateId) {
				return true;
			}
			// add category
			else if (!validateId) {
				return true;
			}
		}
		return false;
	}

	private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
		Category category = new Category();
		if (isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
			category.setName(requestMap.get("name"));

		return category;

	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filerValue) {
		
		try
		{
			
			if(filerValue!=null)
			{
				return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(),HttpStatus.OK);
			}
			return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);
		}
		catch (Exception e) {
			// TODO: handle exception
			log.error("CategoryServiceImpl:getAllCategory",e);
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		
		try {
			
			if(jwtFilter.isAdmin())
			{
				if(validateCategoryMap(requestMap, true))
				{
					Optional optional = categoryRepository.findById(Integer.parseInt(requestMap.get("id")));
	
					if(optional!=null&&optional.empty()!=optional)
					{
						categoryRepository.save(getCategoryFromMap(requestMap, true));
						return ExceptionUtils.getReponseEntity("Category "+Message.UPDATED, HttpStatus.OK);
						
					}
					return ExceptionUtils.getReponseEntity("Category "+Message.NOT_FOUND, HttpStatus.OK);
				}
				return ExceptionUtils.getReponseEntity(Message.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
			else
			{
				return ExceptionUtils.getReponseEntity(Message.UNATHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
