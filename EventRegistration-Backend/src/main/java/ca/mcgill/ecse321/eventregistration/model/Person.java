package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name= "person")
public class Person{

	public Person(String aName, RegistrationManager aRegistrationManager, CreditCard aCreditCard) {
		// TODO Auto-generated constructor stub
		this.name = aName;
		this.registrationManager = aRegistrationManager;
		this.creditCard = aCreditCard;
	}
	
	public Person(String name, CreditCard creditCard) {
		this.name = name;
		this.creditCard = creditCard;
	}
	public Person() {
		this.name = null;
		this.creditCard = null;
	}
	
	public Person(String name) {
		this.name = name;
	}

	//manually added
	private CreditCard creditCard;
	
	@OneToOne(optional = true, cascade = CascadeType.PERSIST)
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JoinColumn(name = "accountNumber")
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	private RegistrationManager registrationManager;

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}
	
	@ManyToOne(cascade = { CascadeType.ALL })
	public RegistrationManager getRegistrationManager() {
		return registrationManager;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	private String name;

	public void setName(String value) {
		this.name = value;
	}
	@Id
	public String getName() {
		return this.name;
	}
}
