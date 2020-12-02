package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.eventregistration.model.Promoter;

@Repository
public interface PromoterRepository extends CrudRepository<Promoter, String> {
	
	Promoter findPromoterByname (String name);

}
