package com.blog.blogpessoal.controllers;

import java.util.List;
import java.util.Optional;

import javax.naming.spi.DirStateFactory.Result;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blogpessoal.model.User;
import com.blog.blogpessoal.model.UserLogin;
import com.blog.blogpessoal.repositories.UserRepository;
import com.blog.blogpessoal.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<UserLogin> authorization(@RequestBody Optional<UserLogin> user) {
		
		
		return userService.login(user)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<Optional<User>> register(@Valid @RequestBody User user) {
		
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(userService.userRegister(user));
	}
	
	
	@PutMapping("/update")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
		
		return userService.updateUser(user)
				.map( response -> ResponseEntity.status(HttpStatus.OK).body(response))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.ok(userRepository.findAll());
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		
		return userRepository.findById(id)
				.map( res -> ResponseEntity.ok(res))
				.orElse(ResponseEntity.notFound().build());
	}
}
