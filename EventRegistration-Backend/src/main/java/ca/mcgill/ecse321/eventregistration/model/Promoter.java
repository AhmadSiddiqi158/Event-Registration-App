package ca.mcgill.ecse321.eventregistration.model;


import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import javax.persistence.OneToMany;


@Entity
public class Promoter extends Person{

	public Promoter(String aName, RegistrationManager aRegistrationManager, CreditCard aCreditCard) {
		super(aName, aRegistrationManager, aCreditCard);

	}
	
	public Promoter () {
		super ();		
	}
	
	public Promoter (String name) {
		super (name);		
	}
	
	List<Event> promotes;
	
	
	public void setPromotes(List<Event> promotes) {
		this.promotes = promotes;
	}
	
	
	@OneToMany(cascade = { CascadeType.ALL })
	@ElementCollection(targetClass=Event.class)
	@Column
	public List<Event> getPromotes() {
		
		return promotes;
	}
	
	

	



}