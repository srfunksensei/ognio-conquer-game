package com.mb.controller;

import com.mb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

	private final UserService userService;
	
	@GetMapping(path = "/{id}/points")
	public HttpEntity<Long> getUserPoints(@PathVariable(name = "id") final long userId) {
		final long points = userService.getPoints(userId);
		return new ResponseEntity<>(points, HttpStatus.OK);
	}
}
