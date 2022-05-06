package com.blog.blogpessoal.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.blogpessoal.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	public Optional<User> findByUser(String user);
	
	public List<User> findAllByNameContainingIgnoreCase(String name);
}
