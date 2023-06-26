package com.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.constants.Message;
import com.app.exception.ExceptionUtils;
import com.app.jwt.CustomUserDetailsService;
import com.app.jwt.JwtFilter;
import com.app.jwt.JwtUtil;
import com.app.modal.User;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import com.app.util.EmailSendingUtils;
import com.app.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private EmailSendingUtils emailUtils;
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		log.info("Inside Service SignUp Method...{}", requestMap);

		try {
			if (validateSignUp(requestMap)) {
				User email = userRepository.findByEmail(requestMap.get("email"));
				log.info("Username:{}", email);
				if (email == null) {
					userRepository.save(getUserFromMap(requestMap));
					
					return ExceptionUtils.getReponseEntity(Message.SUCCESS_REGISTERED, HttpStatus.OK);

				} else {
					return ExceptionUtils.getReponseEntity(Message.EMAIL_ALREADY_EXITS, HttpStatus.BAD_REQUEST);
				}
			} else {
				return ExceptionUtils.getReponseEntity(Message.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
	}

	private boolean validateSignUp(Map<String, String> requestMap) {
		if (requestMap.containsKey("name") && requestMap.containsKey("email") && requestMap.containsKey("mobile")
				&& requestMap.containsKey("password")) {
			return true;
		}

		else {
			return false;
		}
	}

	private User getUserFromMap(Map<String, String> requestMap) {
		User user = new User();
		user.setEmail(requestMap.get("email"));
		user.setName(requestMap.get("name"));
		user.setMobile(requestMap.get("mobile"));
		user.setPassword(requestMap.get("password"));
		user.setRole("user");
		user.setStatus("false");

		return user;

	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
	
		log.info("Inside Login...");
		try
		{
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
			
			if(authenticate.isAuthenticated())
			{
				if(customUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true"))
				{
					
					return new ResponseEntity<String>("{\"token\":\""+jwtUtil.generateToken(customUserDetailsService.getUserDetails().getEmail(), customUserDetailsService.getUserDetails().getRole())+"\"}",HttpStatus.OK);
					
				}
				else
				{
					return new ResponseEntity<String>("{\"message\":\"Wait for admin Approval\"}",HttpStatus.BAD_REQUEST);
				}
			}
		}
		catch (Exception e) {
			log.error("{}",e);
		}
		 return new ResponseEntity<String>("{\"message\":\"Bad Credentails\"}",HttpStatus.BAD_REQUEST);
		
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			if (jwtFilter.isAdmin()) {
				return new ResponseEntity<>(userRepository.getAllUser(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error("{}", e);
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		
		try
		{
			if(jwtFilter.isAdmin())
			{
				Optional<User> id = userRepository.findById(Integer.parseInt(requestMap.get("id")));
				
				if(id.isPresent())
				{
					userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					sendMailToAllAdmin(requestMap.get("status"),id.get().getEmail(),userRepository.getAllAdmin());
					return ExceptionUtils.getReponseEntity("Status Update Successfully...", HttpStatus.OK);
					
				}
				else
				{
					return ExceptionUtils.getReponseEntity("Id Does not exits", HttpStatus.NOT_FOUND);
				}
			}
			else
			{
				return ExceptionUtils.getReponseEntity(Message.UNATHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		}
		catch (Exception e) {
		log.error("{}",e);
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
		
		System.out.println(allAdmin);
		
		allAdmin.remove(jwtFilter.getCurrentUser());
		
		System.out.println(allAdmin);
		
		if(status!=null && status.equalsIgnoreCase("true"))
		{
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "User : "+user+"\n is Approved By Admin : "+jwtFilter.getCurrentUser(), allAdmin);
		}
		else
		{
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled", "User : "+user+"\n is Disabled By Admin : "+jwtFilter.getCurrentUser(), allAdmin);

		}
		
	}

	@Override
	public ResponseEntity<String> checkToken() {
		
		return ExceptionUtils.getReponseEntity("true", HttpStatus.OK);
	}
	

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		
		try
		{
			User currentUser = userRepository.findByEmail(jwtFilter.getCurrentUser());
			
			if(!currentUser.equals(null))
			{
				if(currentUser.getPassword().equals(requestMap.get("oldPassword")))
				{
					currentUser.setPassword(requestMap.get("newPassword"));
					userRepository.save(currentUser);
					
					return ExceptionUtils.getReponseEntity("Password updated successfully", HttpStatus.OK);
				}
				
				return ExceptionUtils.getReponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
				
			}
			// if not found the user
			return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		catch (Exception e) {
			log.error("changePassword {}",e);
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgetPassword(Map<String, String> requestMap) {
		
		
		try
		{
			User user=userRepository.findByEmail(requestMap.get("email"));
			if(user!=null)
			{
				emailUtils.forgetMail(user.getEmail(), "Credentials by CCTV Management", user.getPassword());
				return ExceptionUtils.getReponseEntity("Check your email for Credentials: ", HttpStatus.OK);
			}
			return ExceptionUtils.getReponseEntity("Does not exits given email id", HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			log.error("changePassword {}",e);
		}
		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
