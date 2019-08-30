package com.mb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mb.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	private UserService userService;
	
	@Autowired
	public UserController(final UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping(path = "/{id}")
	public HttpEntity<Long> getUser(@PathVariable(name = "id") long userId) {
		long points = userService.getPoints(userId);
		return new ResponseEntity<>(points, HttpStatus.OK);
	}
}
