package com.github.coder229.todo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.coder229.todo.ItemService;
import com.github.coder229.todo.JpaService;
import com.github.coder229.todo.entity.Item;

import static org.junit.Assert.*;

public class ItemServiceTest extends ItemService {
	
	static EntityManagerFactory factory;
	EntityManager entityManager;
	JpaService jpaService = new JpaService();
	ItemService service = new ItemService();

	@BeforeClass
	public static void beforeClass() {
		factory = Persistence.createEntityManagerFactory("test");
	}
	
    @Before
    public void beforeEach() {
		entityManager = factory.createEntityManager();
		
    	jpaService.setFactory(factory);
    	service.setJpaService(jpaService);
    	
    	createItem("Item One" + System.currentTimeMillis());
    	createItem("Item Two" + System.currentTimeMillis());
    }
    
	@Test
	public void testGetAllItems() {
		Response response = service.getAllItems();
		
		assertNotNull(response);
		assertEquals(200, response.getStatus());
		
		List<Item> list = (List<Item>) response.getEntity();
		assertTrue(list.size() == 2);
		
		// TODO assert list entries
	}

	@Test
	public void testCreate() {
		Item original = new Item();
		original.setText("First item");
		
		Response response = service.create(original);
		
		assertEquals(200, response.getStatus());
		Item created = (Item) response.getEntity();
		assertEquals(original.getText(), created.getText());
		assertTrue(created.getId() > 0);
	}

	@Test
	public void testUpdate() {
		Item original = new Item();
		original.setText("Second item");
		
		Response response = service.create(original);
		
		assertEquals(200, response.getStatus());
		Item created = (Item) response.getEntity();
		created.setText("Updated item");
		
		response = service.update(created.getId(), created);
		
		assertEquals(200, response.getStatus());
		Item updated = (Item) response.getEntity();
		assertEquals("Updated item", updated.getText());
		assertTrue(updated.getId() > 0);
	}

	@Test
	public void testGetBudget() {
		long id = createItem("Item to get");
		
		Response response = service.getItem(id);
		
		assertEquals(200, response.getStatus());
		Item found = (Item) response.getEntity();
		assertEquals("Item to get", found.getText());
		assertEquals(id, found.getId());
	}
	
	private long createItem(String text) {
		Item item = new Item();
    	item.setText(text);
    	entityManager.getTransaction().begin();
    	entityManager.persist(item);
    	entityManager.flush();
    	entityManager.getTransaction().commit();
    	
    	return item.getId();
	}
}
