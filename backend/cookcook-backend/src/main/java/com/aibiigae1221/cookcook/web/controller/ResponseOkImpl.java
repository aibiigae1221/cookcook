package com.aibiigae1221.cookcook.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.aibiigae1221.cookcook.util.HashMapBean;

@Component
public class ResponseOkImpl implements ResponseOk{

	@Override
	public ResponseEntity<?> ok(HashMapBean mapHolder) {
		mapHolder.put("status", "success");
		return ResponseEntity.status(HttpStatus.OK).body(mapHolder.getSource());
	}

}
