package com.aibiigae1221.cookcook.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class HashMapBean {

	private Map<String, Object> map;
	
	public HashMapBean() {
		map = new HashMap<String, Object>();
	}
	
	public void put(String key, Object obj) {
		map.put(key, obj);
	}
	
	public Object get(String key) {
		return map.get(key);
	}

	@Override
	public String toString() {
		return map
				.keySet()
				.stream()
				.map(keyname -> map.get(keyname).toString())
				.collect(Collectors.joining(" / "));
	}

	public Object getSource() {
		return map;
	}
}
