package com.github.coder229.todo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.github.coder229.todo.entity.Identifiable;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
public class JpaService {
	private static final Logger LOG = Logger.getLogger(JpaService.class);
	private EntityManagerFactory factory;
	
	public void setFactory(EntityManagerFactory factory) {
		this.factory = factory;
	}

	public EntityManager getEntityManager() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("todo");
		}
		return factory.createEntityManager();
	}

	public <T> T getSingleValue(final String sql, final Class<T> type) {
		EntityManager entityManager = getEntityManager();
		Query query = entityManager.createNativeQuery(sql);
		return type.cast(query.getSingleResult());
	}

	protected <T extends Identifiable> T saveOrUpdate(Identifiable object, Class<T> type) {
		EntityManager entityManager = getEntityManager();
		
		try {
			entityManager.getTransaction().begin();
			
			if (object.getId() > 0) {
				object = entityManager.merge(object);
			} else {
				entityManager.persist(object);
			}
			
			entityManager.getTransaction().commit();
		} finally {
			entityManager.close();
		}
		return type.cast(object);
	}

	protected <T> List<T> getListFromNamedQuery(String name, Class<T> type) {
		EntityManager entityManager = getEntityManager();
		@SuppressWarnings("unchecked")
		List<T> results = entityManager.createNamedQuery(name).getResultList();
		LOG.info("Query " + name + " found " + results.size() + " rows.");
		entityManager.close();
		return results;
	}


	public <T> T getSingleFromNamedQuery(String name, Map<String, Object> params, Class<T> type) {
		EntityManager entityManager = getEntityManager();
		TypedQuery<T> query = entityManager.createNamedQuery(name, type);
		for (Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
		return query.getSingleResult();
	}
	
	public <T> List<T> getListFromNamedQuery(String name, Integer page, Integer pageSize, Class<T> type) {
		EntityManager entityManager = getEntityManager();
		Query query = entityManager.createNamedQuery(name);
		
		if (pageSize != null) {
			query.setMaxResults(pageSize);
			
			if (page != null) {
				query.setFirstResult(page * pageSize);
			}
		}
		
		@SuppressWarnings("unchecked")
		List<T> results = query.getResultList();
		LOG.info("Query " + name + " found " + results.size() + " rows.");
		entityManager.close();
		return results;
	}

	public int delete(final String queryName, final Long id) {
		int deleted = 0;
		EntityManager entityManager = getEntityManager();
		try {
			entityManager.getTransaction().begin();
			
			Query query = entityManager.createNamedQuery(queryName);
			query.setParameter("id", id);
			deleted = query.executeUpdate();
			
			entityManager.getTransaction().commit();
		} finally {
			entityManager.close();
		}
		return deleted;
	}
}