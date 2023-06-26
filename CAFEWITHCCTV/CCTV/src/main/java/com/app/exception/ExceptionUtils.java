package com.app.exception;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionUtils {

	private ExceptionUtils() {

	}

	public static ResponseEntity<String> getReponseEntity(String responseMsg, HttpStatus httpStatus) {
		return new ResponseEntity<String>("{\"message\":\"" + responseMsg + "\"}", httpStatus);
	}

	
}
