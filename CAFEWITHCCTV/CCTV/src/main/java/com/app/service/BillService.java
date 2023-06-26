package com.app.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface BillService {

	ResponseEntity<String> generateBill(Map<String, String> requestMap);

}
