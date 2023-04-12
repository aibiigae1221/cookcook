package com.aibiigae1221.cookcook.web.controller;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aibiigae1221.cookcook.util.HashMapBean;

@RestController
public class CommonController {

	@Value("${user-resource-server-url}")
	private String resourceServerUrl;
	
	@Autowired
	private ObjectProvider<HashMapBean> hashMapHolderProvider;
	
	@Autowired
	private ResponseOk responseOk;
	
	
	@GetMapping("/generic/resource-server-url")
	public ResponseEntity<?> getResourceServerUrl() {
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		mapHolder.put("url", resourceServerUrl);
		return responseOk.ok(mapHolder);

	}
	
}
