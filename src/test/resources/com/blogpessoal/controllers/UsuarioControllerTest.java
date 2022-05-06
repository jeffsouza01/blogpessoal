package com.blogpessoal.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blog.blogpessoal.model.User;
import com.blog.blogpessoal.repositories.UserRepository;
import com.blog.blogpessoal.service.UserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeAll
	void start() {
		userRepository.deleteAll();
	}
	

	
	@Test
	@Order(1)
	@DisplayName("Register a new User")
	public void shouldBeAbleToRegisterNewUser() {
		HttpEntity<User> request = new HttpEntity<User>(new User(
				0L, "Jamal", "jamal_souza@gmail.com", "test_password" ,"http://imgur.com/2135.jpg"
				));
		
		// metodo que ira testar o metodo post com as informações de usuario
		// necessário 4 parametros - ENDPOINT, METODO USADO, REQUEST, RETORNO ESPERADO
		ResponseEntity<User> response = testRestTemplate
				.exchange("/users/register", HttpMethod.POST, request, User.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(request.getBody().getName(), response.getBody().getName());
		assertEquals(request.getBody().getUser(), response.getBody().getUser());
		assertEquals(request.getBody().getPhoto(), response.getBody().getPhoto());
	}
	
	
	@Test
	@Order(2)
	@DisplayName("Dont create a duplicate user")
	public void shouldNotBeAbleToRegisterNewUser() {
		userService.userRegister(new User(
				0L, "Jeff", "jeffsouza01@gmail.com", "test_password" ,"http://imgur.com/2135.jpg"
				));
		HttpEntity<User> request = new HttpEntity<User>(new User(
				0L, "Jeff", "jeffsouza01@gmail.com", "test_password" ,"http://imgur.com/2135.jpg"
				));

		ResponseEntity<User> responseEntity = testRestTemplate
				.exchange("/users/register", HttpMethod.POST, request, User.class);
		
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(request.getBody().getName(), response.getBody().getName());
		assertEquals(request.getBody().getUser(), response.getBody().getUser());
		assertEquals(request.getBody().getPhoto(), response.getBody().getPhoto());
		
	}
	
	
	@Test
	@Order(3)
	@DisplayName("Update a user")
	public void shouldBeAbleToUpdateaUser() {
		
		Optional<User> createUser = userService.userRegister(new User(
				0L, "Jeff", "jeffsouza01@gmail.com", "test_password" ,"http://imgur.com/2135.jpg"
				));
		User updateUser = new User(createUser.get().getId(),
				"Jefferson Souza", "je_jss@hotmail.com", "new_password", "http://imgur.com/2255.jpg");
		
		HttpEntity<User> request = new HttpEntity<User>(updateUser);
		
		ResponseEntity<User> response= testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/users/register", HttpMethod.PUT, request, User.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updateUser.getName(), response.getBody().getName());
		assertEquals(updateUser.getUser(), response.getBody().getUser());
		assertEquals(updateUser.getPhoto(), response.getBody().getPhoto());
	}
	
}
