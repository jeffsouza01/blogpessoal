package com.blog.blogpessoal.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tb_user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 80)
	private String name;
	
	@NotNull
	@Size(min = 3, max = 80)
	private String user;
	
	
	@NotNull
	@Size(min = 4)
	private String password;


	private String photo;
	
	
	@OneToMany(mappedBy="user", cascade=CascadeType.REMOVE)
	@JsonIgnoreProperties("user")
	private List<PostagemModel> postagem;
	
	public User(Long id, String name, String user,
			 String password, String photo) {
		this.id = id;
		this.name = name;
		this.user = user;
		this.password = password;
		this.photo = photo;
	}


	public User() {
		
	}
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	
}
