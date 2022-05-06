package com.blog.blogpessoal.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.blogpessoal.model.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long>{
	
	
	public List<Tema> findAllByDescriptionContainingIgnoreCase(String description);
	
}
