package com.app.wrapper;

import lombok.Data;

@Data
public class ProductWrapper {

	private Integer id;

	private String name;

	private String description;

	private Integer price;

	private String status;

	private Integer categoryId;

	private String categoryName;

	public ProductWrapper() {
		// TODO Auto-generated constructor stub
	}

	public ProductWrapper(Integer id, String name, String description, Integer price, String status, Integer categoryId,
			String categoryName) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.status = status;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
//p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name
	

	// p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name)
	

}
