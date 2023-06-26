package com.app.rest;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.constants.Message;
import com.app.exception.ExceptionUtils;
import com.app.service.BillService;

@RestController
@RequestMapping("/bill")
public class BillRestController {
	
	@Autowired
	private BillService billService;
	
	@PostMapping("/generateReport")
	public ResponseEntity<String> generateBill(@RequestBody Map<String, String>requestMap)
	{
		
		try {
			
			return billService.generateBill(requestMap);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}

