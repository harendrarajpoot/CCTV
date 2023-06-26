package com.app.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Table
@Entity
@DynamicInsert
@DynamicUpdate
@Data
public class Bill {	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="uuid")
	private String uuid;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email")
	private String email;
	
	@Column(name="contact")
	private String contact;
	
	@Column(name="paymentMethod")
	private String paymentMethod;
	
	@Column(name="total")
	private Integer total;
	
	@Column(name="productDetails",columnDefinition="json")
	private String productDetails;
	
	@Column(name="createdBy")
	private String createdBy;
}
