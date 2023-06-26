package com.app.repository;

import java.util.List;

import javax.persistence.NamedQuery;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.modal.User;
import com.app.wrapper.UserWrapper;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String name);

	List<UserWrapper> getAllUser();

	@Transactional
	@Modifying
	Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

	List<String> getAllAdmin();
	
	

}
