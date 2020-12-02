package ca.mcgill.ecse321.eventregistration.model;

import java.util.*;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity

public class CreditCard {

	public CreditCard(String aCardID, double aAmount, Person aPerson)
	{
		this.cardID = aCardID;
		this.amount = aAmount;
		if (aPerson == null || aPerson.getCreditCard() != null)
		{
			throw new RuntimeException("Unable to create CreditCard due to aPerson.");
		}
		this.person = aPerson;
		this.registrations = new ArrayList<Registration>();
	}

	public CreditCard(String aCardID, double aAmount, String aNameForPerson, RegistrationManager aRegistrationManagerForPerson)
	{
		cardID = aCardID;
		amount = aAmount;
		person = new Person(aNameForPerson, aRegistrationManagerForPerson, this);
		registrations = new ArrayList<Registration>();
	}

	//------------------------
	// INTERFACE
	//------------------------
	private String cardID;

	public void setCardID(String aCardID) {
		this.cardID = aCardID;
	}
	
	@Id
	public String getCardID() {
		return cardID;
	}


	private double amount;

	public void setAmount(double aAmount) {
		this.amount = aAmount;
	}
	
	public double getAmount() {
		return amount;
	}

	private Person person;
	
	@OneToOne(mappedBy="creditCard")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	
	private List<Registration> registrations;
	
	@ElementCollection
	public List<Registration> getRegistrations() {
		List<Registration> newRegistrations = Collections.unmodifiableList(registrations);
		return newRegistrations;
	}
	
	public void setRegistrations(List<Registration> registrations) {
		
		this.registrations = registrations;
	}


}