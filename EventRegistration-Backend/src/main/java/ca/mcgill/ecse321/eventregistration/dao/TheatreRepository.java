package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.eventregistration.model.Theatre;

@Repository
public interface TheatreRepository extends CrudRepository<Theatre, String> {

	Theatre findTheatreBytitle(String title);

}

