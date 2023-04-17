package com.aibiigae1221.cookcook.web.controller.auxiliary;

import org.springframework.http.ResponseEntity;

import com.aibiigae1221.cookcook.util.HashMapBean;

public interface ResponseOk {
	ResponseEntity<?> ok(HashMapBean mapHolder);
}
