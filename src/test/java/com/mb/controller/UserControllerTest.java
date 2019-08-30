package com.mb.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.mb.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService userServiceMock;
	
	@Test
	public void givenUser_whenGetUser_thenReturnNumber() throws Exception {
		final long id = 1;
		when(userServiceMock.getPoints(anyLong())).thenReturn(0L);

		mvc.perform(get("/users/" + id) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(content().string("0"));
		
		verify(userServiceMock, times(1)).getPoints(anyLong());
		verifyNoMoreInteractions(userServiceMock);
	}
}
