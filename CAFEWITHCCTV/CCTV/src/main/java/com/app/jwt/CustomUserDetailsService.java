package com.app.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.modal.User;
import com.app.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	private User user;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loaduserByUsername {}",username);
		user = userRepository.findByEmail(username);
		
		if(user!=null)
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
		else
			throw new UsernameNotFoundException("User Not Found...");
	}

	public User getUserDetails() {
		return user;
	}

}
