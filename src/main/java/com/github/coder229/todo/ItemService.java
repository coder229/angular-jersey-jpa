package com.github.coder229.todo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.github.coder229.todo.entity.Item;
import com.github.coder229.todo.util.MapUtil;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class ItemService {
	private static final Logger LOG = Logger.getLogger(ItemService.class);
	
	@InjectParam JpaService jpaService;

	@GET
	public Response getAllItems() {
		List<Item> list = jpaService.getListFromNamedQuery(Item.GET_ALL, Item.class);
		
		LOG.info("Found " + list.size() + " budgets");
		return Response.ok(list).build();
	}

	@POST
	@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response create(Item item) {
		LOG.info("Creating item " + item.getText());
		
		if (item.getCreated() == null) {
			item.setCreated(new Date());
		}

		try {
			item = jpaService.saveOrUpdate(item, Item.class);
			return Response.ok(item).build();
		} catch (Exception e) {
			return Response.status(500).entity(e.getLocalizedMessage()).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") Long id, Item item) {
		LOG.info("Updating item " + item.getText());
		
		try {
			item = jpaService.saveOrUpdate(item, Item.class);
			return Response.ok(item).build();
		} catch (Exception e) {
			return Response.status(500).entity(e.getLocalizedMessage()).build();
		}
	}

	@GET
	@Path("/{id}")
	public Response getItem(@PathParam("id") Long id) {
		Map<String, Object> params = MapUtil.create("id", id).build();
		Item item = jpaService.getSingleFromNamedQuery(Item.GET_BY_ID, params, Item.class);
		LOG.info("Found " + item.getText());
		return Response.ok(item).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteItem(@PathParam("id") Long id) {
		Integer deleted = jpaService.delete(Item.DELETE_BY_ID, id);
		LOG.info("Deleted " + deleted + " item(s)");
		return Response.ok(deleted).build();
	}
	
	public void setJpaService(JpaService jpaService) {
		this.jpaService = jpaService;
	}

}
