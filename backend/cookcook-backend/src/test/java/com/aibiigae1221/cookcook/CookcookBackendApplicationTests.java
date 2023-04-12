package com.aibiigae1221.cookcook;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class CookcookBackendApplicationTests {


	@Autowired
	private MockMvc mvc;
	
	
	@Test
	public void test1() throws Exception {
		mvc.perform(get("/generic/resource-server-url"))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
