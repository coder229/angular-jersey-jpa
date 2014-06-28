package com.github.coder229.todo;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestEntityManagerUtil {
	private static EntityManagerFactory factory = null;
	
    public static EntityManagerFactory getEntityManagerFactory() {
    	if (factory == null) {
    		factory = Persistence.createEntityManagerFactory("test");
    	}
    	return factory;
    }
}