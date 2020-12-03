package ca.mcgill.ecse321.eventregistration.model;


import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Promoter extends Person{

//	public Promoter(String aName, RegistrationManager aRegistrationManager, CreditCard aCreditCard) {
//		super(aName, aRegistrationManager, aCreditCard);
//
//	}
//	
//	public Promoter () {
//		super ();		
//	}
//	
//	public Promoter (String name) {
//		super (name);		
//	}
	
	@OneToMany(cascade = { CascadeType.ALL })
	@ElementCollection(targetClass=Event.class)
	@Column
	List<Event> promotes;
	
	@Transient
	public void setPromoters(List<Event> promotes) {
		this.promotes = promotes;
	}
	
	@Transient
	public List<Event> getPromotes() {
		
		return promotes;
	}
	
	

	



}