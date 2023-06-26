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
import org.springframework.web.bind.annotation.RestController;

import com.app.constants.Message;
import com.app.exception.ExceptionUtils;
import com.app.modal.User;
import com.app.service.UserService;
import com.app.wrapper.UserWrapper;

@RestController
@RequestMapping("/user")
public class UserRestContoller {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap) {

		try {
		 return userService.signUp(requestMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/login")
	public ResponseEntity<String>login(@RequestBody(required=true) Map<String,String>requestMap)
	{try {
		 return userService.login(requestMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/get")
	public ResponseEntity<List<UserWrapper>>getAllUser()
	{
		try
		{
			
		return userService.getAllUser();	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PostMapping("/update")
	public ResponseEntity<String>update(@RequestBody Map<String, String>requestMap)
	{
		try
		{
		 return userService.update(requestMap);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/checkToken")
	public ResponseEntity<String>checkToken()
	{
		try
		{
		 return userService.checkToken();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@PostMapping("/changePassword")
	public ResponseEntity<String>changePassword(@RequestBody Map<String, String>requestMap)
	{
		try
		{
		 return userService.changePassword(requestMap);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/forgetPassword")
	public ResponseEntity<String>forgetPassword(@RequestBody Map<String, String>requestMap)
	{
		try
		{
		 return userService.forgetPassword(requestMap);
		 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
