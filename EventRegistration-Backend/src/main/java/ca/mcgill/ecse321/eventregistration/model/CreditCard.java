package ca.mcgill.ecse321.eventregistration.model;

import java.util.*;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;


@Entity

public class CreditCard {

	public CreditCard(String accountNumber, int aAmount, Person aPerson)
	{
		this.accountNumber = accountNumber;
		this.amount = aAmount;
		if (aPerson == null || aPerson.getCreditCard() != null)
		{
			throw new RuntimeException("Unable to create CreditCard due to aPerson.");
		}
		this.person = aPerson;
		this.registrations = new ArrayList<Registration>();
	}

	public CreditCard(String accountNumber, int aAmount, String aNameForPerson, RegistrationManager aRegistrationManagerForPerson)
	{
		accountNumber = accountNumber;
		amount = aAmount;
		person = new Person(aNameForPerson, aRegistrationManagerForPerson, this);
		registrations = new ArrayList<Registration>();
		
	}
	
	public CreditCard (String accountNumber, double amount) {
		this.accountNumber=accountNumber;
		this.amount= (int) amount;
	}
	
	public CreditCard() {
		this.accountNumber = null;
		this.amount = 0;
		this.person = null;
		this.registrations = null;
		
	}

	private String accountNumber;

	public void setAccountNumber(String accoundNumber) {
		this.accountNumber = accoundNumber;
	}
	
	@Id
	public String getAccountNumber() {
		return accountNumber;
	}

	private int amount;

	public void setAmount(double aAmount) {
		this.amount = (int) aAmount;
	}
	
	public int getAmount() {
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
		
		return this.registrations;
	}
	
	public void setRegistrations(List<Registration> registrations) {
		
		this.registrations = registrations;
	}


}