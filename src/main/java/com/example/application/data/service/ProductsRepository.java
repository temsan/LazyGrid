package com.example.application.data.service;

import com.example.application.data.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProductsRepository 
  extends JpaRepository<Products, UUID>, JpaSpecificationExecutor<Products> {

}
