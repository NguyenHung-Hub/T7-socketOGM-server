package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2308036484825496396L;
	@Id
	private int id;
	@Column(name = "category_name", nullable = false)
	private String name;

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Category(int id, String category_name) {
		super();
		this.id = id;
		this.name = category_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory_name() {
		return name;
	}

	public void setCategory_name(String category_name) {
		this.name = category_name;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", category_name=" + name + "]";
	}

}
