package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.sql.Date;
import java.sql.Time;

@Entity
//@Table(name="event")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING, name="TYPE")

public class Event {

    public Event(String aName, Date aDate, Time aStartTime, Time aEndTime, RegistrationManager aRegistrationManager) {
		// TODO Auto-generated constructor stub
    	this.name = aName;
    	this.date = aDate;
    	this.startTime = aStartTime;
    	this.endTime = aEndTime;
    	this.registrationManager = aRegistrationManager;
    	
	}
    
    public Event() {
		// TODO Auto-generated constructor stub
    	this.name = null;
    	this.date = null;
    	this.startTime = null;
    	this.endTime = null;
    	this.registrationManager = null;
    	
	}
    
    private RegistrationManager registrationManager;
    
    public void setRegistrationManager(RegistrationManager registrationManager) {
    	this.registrationManager = registrationManager;
    }
    @ManyToOne (cascade = { CascadeType.ALL })
    public RegistrationManager getRegistrationManager() {
    	return registrationManager;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    private String name;

    public void setName(String value) {
        this.name = value;
    }
    @Id
    public String getName() {
        return this.name;
    }
    private Date date;

    public void setDate(Date value) {
        this.date = value;
    }
    public Date getDate() {
        return this.date;
    }
    private Time startTime;

    public void setStartTime(Time value) {
        this.startTime = value;
    }
    public Time getStartTime() {
        return this.startTime;
    }
    private Time endTime;

    public void setEndTime(Time value) {
        this.endTime = value;
    }
    public Time getEndTime() {
        return this.endTime;
    }
}
