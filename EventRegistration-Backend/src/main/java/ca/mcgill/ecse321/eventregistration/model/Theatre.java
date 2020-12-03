package ca.mcgill.ecse321.eventregistration.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.sql.Date;
import java.sql.Time;


import javax.persistence.Entity;


// line 35 "model.ump"
// line 84 "model.ump"
@Entity
public class Theatre extends Event {

  public Theatre(String aName, Date aDate, Time aStartTime, Time aEndTime, RegistrationManager aRegistrationManager, String aTitle)
  {
    super(aName, aDate, aStartTime, aEndTime, aRegistrationManager);
    title = aTitle;
  }
  public Theatre(String aName, Date aDate, Time aStartTime, Time aEndTime, String aTitle)
  {
    super(aName, aDate, aStartTime, aEndTime);
    title = aTitle;
  }
  
  public Theatre() {
	  super();
  }
  //------------------------
  // INTERFACE
  //------------------------
  private String title;

  public void setTitle(String aTitle)
  {
    //boolean wasSet = false;
    this.title = aTitle;
    //wasSet = true;
    //return title;
  }
  
  public String getTitle()
  {
    return title;
  }


}
