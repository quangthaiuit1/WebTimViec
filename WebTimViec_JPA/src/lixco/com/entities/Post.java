package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "post")
public class Post  extends AbstractEntities{
	private String name;
	private long salaryFrom;
	private long salaryTo;
	private String shortDecription;
	private String detail;
	private String contactPeople;
	private int contactPhone;
	private String contactEmail;

	@OneToOne
	private User user;
	
	@OneToOne
	private Location location;
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public long getSalaryFrom() {
		return salaryFrom;
	}



	public void setSalaryFrom(long salaryFrom) {
		this.salaryFrom = salaryFrom;
	}



	public long getSalaryTo() {
		return salaryTo;
	}



	public void setSalaryTo(long salaryTo) {
		this.salaryTo = salaryTo;
	}



	public String getDetail() {
		return detail;
	}



	public void setDetail(String detail) {
		this.detail = detail;
	}



	public String getContactPeople() {
		return contactPeople;
	}



	public void setContactPeople(String contactPeople) {
		this.contactPeople = contactPeople;
	}



	public int getContactPhone() {
		return contactPhone;
	}



	public void setContactPhone(int contactPhone) {
		this.contactPhone = contactPhone;
	}



	public String getContactEmail() {
		return contactEmail;
	}



	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public Location getLocation() {
		return location;
	}



	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post p = (Post) obj;
		if (id != p.id)
			return false;
		return true;
	}



	public String getShortDecription() {
		return shortDecription;
	}



	public void setShortDecription(String shortDecription) {
		this.shortDecription = shortDecription;
	}
	
}
