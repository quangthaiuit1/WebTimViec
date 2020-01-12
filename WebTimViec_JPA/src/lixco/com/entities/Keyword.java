package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "keyword")
public class Keyword extends AbstractEntities{
	private String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
