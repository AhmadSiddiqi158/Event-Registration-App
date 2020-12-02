package ca.mcgill.ecse321.eventregistration.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.sql.Date;
import java.sql.Time;
import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Id;

// line 35 "model.ump"
// line 84 "model.ump"
@Entity
public class Theatre extends Event
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Theatre Attributes
  private String title;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Theatre(String aName, Date aDate, Time aStartTime, Time aEndTime, RegistrationManager aRegistrationManager, String aTitle)
  {
    super(aName, aDate, aStartTime, aEndTime, aRegistrationManager);
    title = aTitle;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTitle(String aTitle)
  {
    boolean wasSet = false;
    title = aTitle;
    wasSet = true;
    return wasSet;
  }
  
  public String getTitle()
  {
    return title;
  }


}
