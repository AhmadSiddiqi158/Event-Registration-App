package ca.mcgill.ecse321.eventregistration.service.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPromoterRole {
    @Autowired
    private EventRegistrationService service;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private PromoterRepository promoterRepository;

    @After
    public void clearDatabase() {
        // First, we clear registrations to avoid exceptions due to inconsistencies
        registrationRepository.deleteAll();
        // Then we can clear the other tables
        personRepository.deleteAll();
        eventRepository.deleteAll();
        promoterRepository.deleteAll();
    }

    @Test
    public void test_01_CreatePromoter() {
        try {
            String name = "validname";
            service.createPromoter(name);
            List<Promoter> promoters = service.getAllPromoters();
            assertEquals(promoters.size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_02_CreatePromoterWithEmptyName() {
        try {
            String name = "";
            service.createPromoter(name);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Promoter name cannot be empty!", e.getMessage());
            List<Promoter> promoters = service.getAllPromoters();
            assertEquals(promoters.size(), 0);
        }
    }

    @Test
    public void test_04_CreatePromoterDuplicate() {
        try {
            String name = "validname";
            service.createPromoter(name);
            List<Promoter> promoters = service.getAllPromoters();
            assertEquals(promoters.size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }

        try {
            String name = "validname";
            service.createPromoter(name);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Promoter has already been created!", e.getMessage());
            List<Promoter> promoters = service.getAllPromoters();
            assertEquals(promoters.size(), 1);
        }
    }

    @Test
    public void test_05_PromotesEvent() {
        try {
            Promoter promoter = service.createPromoter("validname");
            Event event = PromoterRoleTestData.setupEvent(service, "eventname");
            service.promotesEvent(promoter, event);
            assertEquals(promoter.getPromotes().size(), 1);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_06_PromotesEventWithNullPromoter() {
        try {
            Promoter promoter = null;
            Event event = PromoterRoleTestData.setupEvent(service, "eventname");
            service.promotesEvent(promoter, event);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Promoter needs to be selected for promotes!", e.getMessage());
        }
    }

    @Test
    public void test_09_PromotesEventWithNonExsistantEvent() {
        try {
            Promoter promoter = service.createPromoter("validname");
            Event event = new Event();
            event.setName("concert");
            service.promotesEvent(promoter, event);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Event does not exist!", e.getMessage());
        }
    }

    @Test
    public void test_10_GetAllPromoters() {
        try {
            service.createPromoter("validname1");
            service.createPromoter("validname2");
            List<Promoter> promoters = service.getAllPromoters();
            assertEquals(promoters.size(), 2);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_11_GetPromoter() {
        try {
            service.createPromoter("promoter");
            service.getPromoter("promoter");
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void test_12_GetPromoterWithNullName() {
        try {
            service.getPromoter(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Person name cannot be empty!", e.getMessage());
        }
    }
}
