package com.mb.controller;

import com.mb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService userServiceMock;
	
	@Test
	public void givenUser_whenGetUserPoints_thenReturnNumber() throws Exception {
		final long userId = 1;
		when(userServiceMock.getPoints(anyLong())).thenReturn(0L);

		mvc.perform(get("/users/" + userId + "/points") //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(content().string("0"));
		
		verify(userServiceMock, times(1)).getPoints(anyLong());
		verifyNoMoreInteractions(userServiceMock);
	}
}
