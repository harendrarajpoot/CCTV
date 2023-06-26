
package com.app.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

/**
 * @author 2199420
 *
 */

@Table
@Entity
@DynamicInsert
@DynamicUpdate
@Data
public class Product {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="category_id" ,nullable=false)
	private Category category;
	
	@Column(name="description")
	private String description;
	
	@Column(name="price")
	private Integer price;
	
	@Column(name="status")
	private String status;

}
