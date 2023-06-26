package com.app.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@NamedQuery(name="User.getAllUser",query="select new com.app.wrapper.UserWrapper(u.id,u.name,u.email,u.mobile,u.status) from User u where u.role='user'")
@NamedQuery(name="User.updateStatus",query="update User u set u.status=:status where u.id=:id")
@NamedQuery(name="User.getAllAdmin",query="select u.email from User u where u.role='admin'")

@DynamicUpdate
@DynamicInsert
@Table
@Data
@Entity
public class User {

	
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String email;
	
	private String mobile;
	
	private String password;
	
	private String role;
	private String status;
	
	

}
