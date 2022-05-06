package com.blogpessoal.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.blog.blogpessoal.model.User;
import com.blog.blogpessoal.repositories.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeAll
	void start() {
		userRepository.deleteAll();
		userRepository.save(new User(0L, "Polvinho", "test_user", "password123", "http://imgur.com/123j.png"));
		userRepository.save(new User(0L, "Bob Esponja", "bob_user", "hamburguer", "http://imgur.com/esponja.png"));
		userRepository.save(new User(0L, "Siri", "siri_user", "siri", "http://imgur.com/siri.png"));
		userRepository.save(new User(0L, "Patrick", "pat_user", "estrela", "http://imgur.com/estrela.png"));

	}
	
	@Test
	@DisplayName("Retorna um usuário")
	public void shouldBeAbleToReturnUser() {
		
		Optional<User> userOptional = userRepository.findByUser("siri");
		assertTrue(userOptional.get().getUser().equals("siri"));
	}
	
	
	@Test
	@DisplayName("Retorna lista de usuários")
	public void shouldBeAbleToReturnManyUsers() {
		
		List<User> userList = userRepository.findAllByNameContainingIgnoreCase("Pol");
		assertEquals(3, userList.size());
		assertTrue(userList.get(0).getName().equals("Polvinho"));
		assertTrue(userList.get(1).getName().equals("Bob Esponja"));
		assertTrue(userList.get(2).getName().equals("Siri"));
	}
	

}
