package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Set;

public class PromoterDto {
	
	private CreditCardDto creditcard;
	private String name;
	//private RegistrationManagerDto registrationManager;
	private Set<EventDto> promotes;
	
	public PromoterDto() {	
	}
	
	public PromoterDto (String name) {
		this.name = name;		
	}

	public CreditCardDto getCreditcard() {
		return creditcard;
	}

	public void setCreditcard(CreditCardDto creditcard) {
		this.creditcard = creditcard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<EventDto> getPromotes() {
		return promotes;
	}

	public void setPromotes(Set<EventDto> promotes) {
		this.promotes = promotes;
	}
	
}
