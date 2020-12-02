package ca.mcgill.ecse321.eventregistration.model;

import java.util.*;

import javax.persistence.Entity;

@Entity
public class Promoter extends Person{

	public Promoter(String aName, RegistrationManager aRegistrationManager, CreditCard aCreditCard) {

		super(aName, aRegistrationManager, aCreditCard);

	}

	



}