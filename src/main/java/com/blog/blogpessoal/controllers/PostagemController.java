package com.blog.blogpessoal.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogpessoal.model.Postagem;
import com.blog.blogpessoal.repositories.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin("*")
public class PostagemController {
	
	@Autowired
	public PostagemRepository postagemRepository;
	
	
	@GetMapping("/all")
	public ResponseEntity<List<Postagem>>getAll() {
		return ResponseEntity.ok(postagemRepository.findAll());
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
		
	
	@GetMapping("/titulo/{title}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String title) {
		return ResponseEntity.ok(postagemRepository.findAllByTitleContainingIgnoreCase(title));
	}
	
	
	@PostMapping
	public ResponseEntity<Postagem> createPost(@RequestBody Postagem postagem) {
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
	}

	
	@PutMapping
	public ResponseEntity<Postagem> updatePost(@RequestBody Postagem postagem) {
		
		return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
	}
	
	@DeleteMapping("{id}")
	public void deletePost(@PathVariable Long id) {
		postagemRepository.deleteById(id);
	}

	/*
	@GetMapping("/all")
	public List<Postagem> allPost() {
		return postagemRepository.findAll();
				 
	}
	
	
	@PostMapping("/new")
	public Postagem newPost(@RequestBody Postagem postagem) {
		Postagem novaPostagem = postagemRepository.save(postagem);
		
		
		return novaPostagem;
	}
	
*/
	
	
	
}
