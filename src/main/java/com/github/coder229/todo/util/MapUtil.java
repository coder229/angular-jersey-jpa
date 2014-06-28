package com.github.coder229.todo.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
	private Map<String,Object> map = new HashMap<String,Object>();
	
	public static MapUtil create() {
		return new MapUtil();
	}
	public static MapUtil create(String key, Object value) {
		return new MapUtil().add(key, value);
	}
	
	public MapUtil add(String key, Object value) {
		map.put(key, value);
		return this;
	}
	
	public Map<String,Object> build() {
		return map; 
	}
}
