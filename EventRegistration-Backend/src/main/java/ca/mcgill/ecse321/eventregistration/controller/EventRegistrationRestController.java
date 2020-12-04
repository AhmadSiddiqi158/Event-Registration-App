package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.dao.PromoterRepository;
import ca.mcgill.ecse321.eventregistration.dto.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;

	// POST Mappings

	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}

	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
					throws IllegalArgumentException {
		// @formatter:on
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}
	/////////////////////////////////////////////////////PROMOTER//////////////////////////////////////////////////////////////////////

	@PostMapping(value = { "/promoters/{name}", "/promoter/{name}/" })
	public PromoterDto createPromoter(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Promoter promoter = service.createPromoter(name);
		return convertToDto(promoter);
	}

	//Create a mapping for promoting an event for the promoters.
	@PostMapping(value = {"/promoteEvent/{name}/{eventName}", "/promoteEvent/{name}/{eventName}"})
	public PromoterDto promoteEvent(@PathVariable("name") String name, @PathVariable("eventName") String eventName ) {
		Promoter promoter = service.getPromoter(name);
		Event event = service.getEvent(eventName);
		
		return (convertToDto(service.promotesEvent(promoter, event)));
	}


	/////////////////////////////////////////////////////Theatre////////////////////////////////////////////////////////////////////

	// http://localhost:8080/theatre/testevent?date=2013-10-23&startTime=00:00&endTime=23:59&title=BBB
	@PostMapping(value = { "/theatres/{name}", "/theatres/{name}/" })
	public TheatreDto createTheatre(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime, @RequestParam String title)
					throws IllegalArgumentException {
		// @formatter:on
		Theatre theatre = service.createTheatre(name, date, Time.valueOf(startTime), Time.valueOf(endTime), title);
		return convertToDto(theatre);
	}

	/////////////////////////////////////////////////////CreditCard////////////////////////////////////////////////////////////////////

	@PostMapping(value = { "/creditCard", "/creditCard/" })
	public CreditCardDto createCreditCard(@RequestParam("accountNumber") String accountNumber, @RequestParam("amount") double amount) throws IllegalArgumentException {
		// @formatter:on
		return convertToDto(service.createCreditCardPay(accountNumber, amount));
	}
	
	@PostMapping(value = { "/pay/{name}/{eventname}", "/pay/{name}/{eventname}" })
	public RegistrationDto pay(@PathVariable("name") String name, @PathVariable("eventname") String eventname,@RequestParam("accountNumber") String accountNumber) throws IllegalArgumentException {
		// @formatter:on
		Registration r = service.getRegistrationByPersonAndEvent(service.getPerson(name), service.getEvent(eventname));
		
		RegistrationDto registrationDto = convertToDto(service.pay(r, service.getCreditCard(accountNumber)));
		
		return registrationDto;
	}
	
	// GET Mappings

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}

	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/events/person/{name}", "/events/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerson(p);
	}

	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}

	@GetMapping(value = { "/registrations", "/registrations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}

	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<RegistrationDto> getRegistrationsForPerson(@PathVariable("name") PersonDto pDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		return createRegistrationDtosForPerson(p);
	}

	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}
	/////////////////////////////////////////////////////PROMOTER////////////////////////////////////////////////////////////////////

	@GetMapping(value = { "/promoters/{name}", "/promoter/{name}/" })
	public PromoterDto getPromoterByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPromoter(name));
	}

	@GetMapping(value = { "/promoters", "/promoters/" })
	public List<PromoterDto> getAllPromoters() {
		List<PromoterDto> promoters = new ArrayList<>();
		for (Promoter promoter : service.getAllPromoters()) {
			promoters.add(convertToDto(promoter));
		}
		return promoters;
	}
	/////////////////////////////////////////////////////Theatre////////////////////////////////////////////////////////////////////

	@GetMapping(value = { "/theatres/{name}", "/theatres/{name}/" })
	public TheatreDto getTheatreByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getTheatre(name));
	}

	@GetMapping(value = { "/theatres", "/theatres/" })
	public List<TheatreDto> getAllTheatres() {
		List<TheatreDto> theatreDtos = new ArrayList<>();
		for (Theatre theatre : service.getAllTheatres()) {
			theatreDtos.add(convertToDto(theatre));
		}
		return theatreDtos;
	}


	/////////////////////////////////////////////////////CreditCard////////////////////////////////////////////////////////////////////
	
	@GetMapping(value = { "/creditCard/{accountNumber}", "/creditCard/{accountNumber}/" })
	public CreditCardDto getCreditCardByaccountNummber(@PathVariable("accountNumber") String accountNumber) throws IllegalArgumentException {
		return convertToDto(service.getCreditCard(accountNumber));
	}


	// Model - DTO conversion methods (not part of the API)

	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		return eventDto;
	}


	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto);
		
		if(r.getCreditCard() != null) {
			CreditCardDto cDTO  = convertToDto(r.getCreditCard());
			rDto.setCreditCard(cDTO);;
			
		}
		return rDto;
	}

	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		if(p.getCreditCard() != null) {
			CreditCardDto cDTO = convertToDto(p.getCreditCard());
			personDto.setCreditCard(cDTO);
		}
		return personDto;
	}
	
	//DTOs for Promoters
	private PromoterDto convertToDto(Promoter p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Promoter!");
		}
		PromoterDto promoterDto = new PromoterDto(p.getName());
		List<EventDto> promotesDto = new ArrayList<EventDto>();
		
		if(p.getPromotes() != null && !p.getPromotes().isEmpty()) {
			for(Event e :p.getPromotes()) {
				promotesDto.add(convertToDto(e));
			}
		}
		promoterDto.setPromotes(promotesDto);
		promoterDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		
		if(p.getCreditCard() != null) {
			CreditCardDto cDTO = convertToDto(p.getCreditCard());
			promoterDto.setCreditcard(cDTO);
		}
		
		return promoterDto;
	}


	//DTO for Theatre
	private TheatreDto convertToDto(Theatre t) {
		if (t == null) {
			throw new IllegalArgumentException("There is no such Theatre!");
		}
		TheatreDto theatreDto = new TheatreDto(t.getName(), t.getDate(), t.getStartTime(), t.getEndTime(), t.getTitle());
		return theatreDto;
	}

	//DTO for CreditCard
	private CreditCardDto convertToDto(CreditCard c) {
		if (c == null) {
			throw new IllegalArgumentException("There is no such CreditCard!");
		}
		CreditCardDto creditCardDto = new CreditCardDto(c.getAccountNumber(), c.getAmount());

//		if(c.getPerson() != null) {
//			PersonDto pDTO = convertToDto(c.getPerson());
//			creditCardDto.setPerson(pDTO);
//		}

		if(c.getRegistrations() != null && !c.getRegistrations().isEmpty()) {
			List<RegistrationDto> rDTO = new ArrayList<RegistrationDto>();
			for(Registration r : c.getRegistrations()) {
				rDTO.add(convertToDto(r));
			}
			creditCardDto.setRegistrations(rDTO);

		}
		return creditCardDto;
	}

	// return registration dto without peron object so that we are not repeating
	// data
	private RegistrationDto convertToDtoWithoutPerson(Registration r) {
		RegistrationDto rDto = convertToDto(r);
		rDto.setPerson(null);
		return rDto;
	}

	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}

	// Other extracted methods (not part of the API)

	private List<EventDto> createAttendedEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}


	private List<EventDto> createPromotedEventDtosForPromoter(Promoter p) {
		List<Event> eventsForPromoter = service.getEventsPromotedByPromoter(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPromoter) {
			events.add(convertToDto(event));
		}
		return events;
	}

	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}
}
