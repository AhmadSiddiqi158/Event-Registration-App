package ca.mcgill.ecse321.eventregistration.service.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.eventregistration.dao.CreditCardRepository;
import ca.mcgill.ecse321.eventregistration.dao.EventRepository;
import ca.mcgill.ecse321.eventregistration.dao.PersonRepository;
import ca.mcgill.ecse321.eventregistration.dao.RegistrationRepository;
import ca.mcgill.ecse321.eventregistration.model.CreditCard;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Person;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPaymentWithCreditCard {
	@Autowired
	private EventRegistrationService service;

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private CreditCardRepository creditCardRepository;

	@After
	public void clearDatabase() {
		// Fisrt, we clear registrations to avoid exceptions due to inconsistencies
		registrationRepository.deleteAll();
		// Then we can clear the other tables
		personRepository.deleteAll();
		eventRepository.deleteAll();
		creditCardRepository.deleteAll();
	}

	@Test
	public void test_01_testPayWithCreditCard() {
		try {
			Person person = TestUtils.setupPerson(service, TestPaymentWithCreditCardData.TEST01_PERSON_NAME);
			Event event = TestUtils.setupEvent(service, TestPaymentWithCreditCardData.TEST01_EVENT_NAME);
			Registration r = TestUtils.register(service, person, event);
			CreditCard ap = service.createCreditCardPay(TestPaymentWithCreditCardData.TEST01_VALID_ID,
					TestPaymentWithCreditCardData.TEST01_VALID_AMOUNT);
			service.pay(r, ap);
			List<Registration> allRs = service.getAllRegistrations();
			assertEquals(allRs.size(), 1);
			creditCardAsserts(ap, allRs.get(0).getCreditCard());

		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void test_04_testMultiplePaysBreakNegative() {
		int breakIndex = TestPaymentWithCreditCardData.TEST04_BREAK_INDEX;
		String[] ids = TestPaymentWithCreditCardData.TEST04_VALID_IDS;
		int[] amounts = TestPaymentWithCreditCardData.TEST04_PARTIAL_BREAK_AMOUNTS;
		String[] names = TestPaymentWithCreditCardData.TEST04_PERSON_NAMES;
		String[] events = TestPaymentWithCreditCardData.TEST04_EVENT_NAMES;

		int length = ids.length;
		CreditCard[] pays = new CreditCard[length];

		try {
			for (int i = 0; i < length; i++) {
				Person person = TestUtils.setupPerson(service, names[i]);
				Event event = TestUtils.setupEvent(service, events[i]);
				Registration r = TestUtils.register(service, person, event);
				pays[i] = service.createCreditCardPay(ids[i], amounts[i]);
				service.pay(r, pays[i]);
			}
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithCreditCardData.AMOUNT_NEGATIVE_ERROR, e.getMessage());
			List<Registration> allRs = service.getAllRegistrations();
			assertEquals(allRs.size(), breakIndex + 1);
			for (int i = 0; i < breakIndex; i++) {
				if (!contains(allRs, pays[i])) {
					fail();
				}
			}
		}
	}

	@Test
	public void test_05_testUpdatePay() {
		try {
			Person person = TestUtils.setupPerson(service, TestPaymentWithCreditCardData.TEST05_PERSON_NAME);
			Event event = TestUtils.setupEvent(service, TestPaymentWithCreditCardData.TEST05_EVENT_NAME);
			Registration r = TestUtils.register(service, person, event);
			CreditCard ap1 = service.createCreditCardPay(TestPaymentWithCreditCardData.TEST05_INITIAL_ID,
					TestPaymentWithCreditCardData.TEST05_INITIAL_AMOUNT);
			CreditCard ap2 = service.createCreditCardPay(TestPaymentWithCreditCardData.TEST05_AFTER_ID,
					TestPaymentWithCreditCardData.TEST05_AFTER_AMOUNT);
			service.pay(r, ap1);
			List<Registration> allRs1 = service.getAllRegistrations();
			assertEquals(allRs1.size(), 1);
			creditCardAsserts(ap1, allRs1.get(0).getCreditCard());
			service.pay(r, ap2);
			List<Registration> allRs2 = service.getAllRegistrations();
			assertEquals(allRs2.size(), 1);
			creditCardAsserts(ap2, allRs2.get(0).getCreditCard());
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void test_06_testCreateCreditCard() {
		try {
			CreditCard ap = service.createCreditCardPay(TestPaymentWithCreditCardData.TEST06_VALID_ID,
					TestPaymentWithCreditCardData.TEST06_VALID_AMOUNT);
			assertEquals(1, creditCardRepository.count());
			for (CreditCard pay : creditCardRepository.findAll()) {
				creditCardAsserts(ap, pay);
			}
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void test_08_testPayWithRegistrationNull() {
		try {
			Person person = TestUtils.setupPerson(service, TestPaymentWithCreditCardData.TEST08_PERSON_NAME);
			Event event = TestUtils.setupEvent(service, TestPaymentWithCreditCardData.TEST08_EVENT_NAME);
			Registration r = TestUtils.register(service, person, event);
			CreditCard ap = null;
			service.pay(r, ap);
			fail();

		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithCreditCardData.PAY_WITH_NULL_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_09_testCreateCreditCardWrongFormat() {
		try {
			service.createCreditCardPay(TestPaymentWithCreditCardData.TEST09_WRONG_ID,
					TestPaymentWithCreditCardData.TEST09_VALID_AMOUNT);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithCreditCardData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_10_testCreateCreditCardLongFormat() {
		try {
			service.createCreditCardPay(TestPaymentWithCreditCardData.TEST10_WRONG_ID,
					TestPaymentWithCreditCardData.TEST10_VALID_AMOUNT);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithCreditCardData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_11_testCreateCreditCardNull() {
		try {
			service.createCreditCardPay(TestPaymentWithCreditCardData.TEST11_WRONG_ID,
					TestPaymentWithCreditCardData.TEST11_VALID_AMOUNT);
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithCreditCardData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_12_testCreateCreditCardEmpty() {
		try {
			service.createCreditCardPay(TestPaymentWithCreditCardData.TEST12_WRONG_ID,
					TestPaymentWithCreditCardData.TEST12_VALID_AMOUNT);
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithCreditCardData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_13_testCreateCreditCardSpace() {
		try {
			service.createCreditCardPay(TestPaymentWithCreditCardData.TEST13_WRONG_ID,
					TestPaymentWithCreditCardData.TEST13_VALID_AMOUNT);
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithCreditCardData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_14_testCreateCreditCardZero() {
		try {
			CreditCard ap = service.createCreditCardPay(TestPaymentWithCreditCardData.TEST14_VALID_ID,
					TestPaymentWithCreditCardData.TEST14_VALID_AMOUNT);
			assertEquals(1, creditCardRepository.count());
			for (CreditCard pay : creditCardRepository.findAll()) {
				creditCardAsserts(ap, pay);
			}
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	// Util Methods, no test
	public void creditCardAsserts(CreditCard expected, CreditCard actual) {
		assertNotEquals(null, actual);
		assertEquals(expected.getAmount(), actual.getAmount());
		assertEquals(expected.getAccountNumber(), actual.getAccountNumber());
	}

	public boolean creditCardEquals(CreditCard pay1, CreditCard pay2) {
		return pay2 != null && pay2.getAmount() == pay1.getAmount()
				&& pay2.getAccountNumber().equals(pay1.getAccountNumber());
	}

	public boolean contains(List<Registration> rs, CreditCard pay) {
		for (Registration r : rs) {
			if (creditCardEquals(pay, r.getCreditCard())) {
				return true;
			}
		}
		return false;
	}
}
