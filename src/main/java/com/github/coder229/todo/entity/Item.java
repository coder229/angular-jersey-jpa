package com.github.coder229.todo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name="Item")
@NamedQueries({
	@NamedQuery(name="Item.ALL", query="select i from Item i order by i.created asc"),
	@NamedQuery(name="Item.GET_BY_ID", query="select distinct i from Item i where i.id = :id"),
	@NamedQuery(name="Item.DELETE_BY_ID", query="delete from Item i where i.id = :id"),
})
public class Item implements Identifiable {
	public static final String GET_ALL = "Item.ALL";
	public static final String DELETE_BY_ID = "Item.DELETE_BY_ID";
	public static final String GET_BY_ID = "Item.GET_BY_ID";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="item_id")
    private long id;
	@Column(name="text")
	private String text;
	@Column(name="created")
	private Date created;
	
	@XmlElement
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@XmlElement
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@XmlElement
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
}
