package com.app.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.modal.Product;
import com.app.wrapper.ProductWrapper;

public interface ProductRepository extends JpaRepository<Product, Integer>{

	@Query("select new com.app.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p")
	 List<ProductWrapper> getAllProduct();
	
	@Modifying
	@Transactional
	@Query("update Product p set p.status=:status where p.id=:id")
	Integer updateStatus(@Param("status")String status,@Param("id") int id);

	@Query("select new com.app.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p where p.category.id=:id")
	List<ProductWrapper> getProductByCategory(@Param("id")Integer id);


	@Query("select new com.app.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p where p.id=:id")
	List<ProductWrapper> getProductById(@Param("id") Integer id);

}
