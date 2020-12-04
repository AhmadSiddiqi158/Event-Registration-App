package ca.mcgill.ecse321.eventregistration.dto;

import java.util.ArrayList;
import java.util.List;



public class CreditCardDto {
	
	private String accountNumber;
	private int amount;
	private PersonDto person;
	private List<RegistrationDto> registrations;
	
	public CreditCardDto(String accountNumber, int aAmount, PersonDto aPerson)
	{
		this.accountNumber = accountNumber;
		this.amount = aAmount;
		if (aPerson == null || aPerson.getCreditCard() != null)
		{
			throw new RuntimeException("Unable to create CreditCard due to aPerson.");
		}
		this.person = aPerson;
		this.registrations = new ArrayList<RegistrationDto>();
	}

	public CreditCardDto(String accountNumber, int aAmount, String person, List<RegistrationDto> registrations)
	{
		this.accountNumber = accountNumber;
		this.amount = aAmount;
		this.person = new PersonDto(person);
		this.registrations = registrations;
		
	}
	
	public CreditCardDto(String accountNumber, double amount) {
		this.accountNumber=accountNumber;
		this.amount= (int) amount;
		this.registrations = new ArrayList<RegistrationDto>();
	}
	
	public CreditCardDto() {
		this.accountNumber = null;
		this.amount = 0;
		this.person = null;
		this.registrations = null;
		
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public PersonDto getPerson() {
		return person;
	}

	public void setPerson(PersonDto person) {
		this.person = person;
	}

	public List<RegistrationDto> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<RegistrationDto> registrations) {
		this.registrations = registrations;
	}
	
}
