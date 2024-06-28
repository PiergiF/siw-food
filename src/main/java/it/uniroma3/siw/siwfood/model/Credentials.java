package it.uniroma3.siw.siwfood.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Credentials {

    public static final String CUSTOMER_ROLE = "CUSTOMER";
    public static final String CHEF_ROLE = "CHEF";
    public static final String ADMINISTRATOR_ROLE = "ADMINISTRATOR";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String username;
	private String password;
	private String role;

	@OneToOne(cascade = CascadeType.ALL)
	private Customer customer;

	@OneToOne(cascade = CascadeType.ALL)
	private Chef chef;

	@OneToOne(cascade = CascadeType.ALL)
	private Administrator administrator;
	

	public String getUsername() {
		return username;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Chef getChef() {
		return chef;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public Administrator getAdministrator() {
		return administrator;
	}

	public void setAdministrator(Administrator administrator) {
		this.administrator = administrator;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}

