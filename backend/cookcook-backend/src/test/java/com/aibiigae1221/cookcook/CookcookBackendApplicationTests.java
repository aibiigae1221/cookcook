package com.aibiigae1221.cookcook;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CookcookBackendApplicationTests {


	@Test
	public void test1() {
		String filename = "asdasd.jpg";
		filename = filename.substring(filename.lastIndexOf("."));
		System.out.println(filename);
		
	}
}
