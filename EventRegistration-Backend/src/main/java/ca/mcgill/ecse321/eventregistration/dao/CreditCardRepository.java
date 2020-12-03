package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.eventregistration.model.CreditCard;

@Repository
public interface CreditCardRepository extends CrudRepository <CreditCard, String> {
	
	CreditCard findCreditCardByaccountNumber(String accountNumber);

}
