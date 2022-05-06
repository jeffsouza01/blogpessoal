package com.blog.blogpessoal.controllers;

import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogpessoal.model.Tema;
import com.blog.blogpessoal.repositories.TemaRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/temas")
public class TemaController {

	@Autowired
	public TemaRepository temaRepository;
	
	
	@GetMapping
	public ResponseEntity<List<Tema>> getAll() {
		return ResponseEntity.ok(temaRepository.findAll());
	}
	
	
	@GetMapping("{id}")
	public ResponseEntity<Tema> getById(@PathVariable Long id) {
		return temaRepository.findById(id)
				.map(result -> ResponseEntity.ok(result))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Tema>>getByName(@PathVariable String name) {
		return ResponseEntity.ok(temaRepository.findAllByDescriptionContainingIgnoreCase(name));
	}
	
	
	@PostMapping
	public ResponseEntity<Tema> saveNewTema(@RequestBody Tema tema) {
		return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(tema));
	}
	
	
	@PutMapping
	public ResponseEntity<Tema> updateTema(@PathVariable Tema tema) {
		return ResponseEntity.status(HttpStatus.OK).body(temaRepository.save(tema));
	}
	
	
	@DeleteMapping("/{id}")
	public void deleteTema(@PathVariable Long id) {
		temaRepository.deleteById(id);
	}
	
}
