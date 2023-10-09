package com.example.application.data.service;

import com.example.application.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsersRepository 
  extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {

}
