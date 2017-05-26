package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Concept;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Integer> {
	
	@Query("select c from Concept c where c.crown.userAccount.id=?1 order by c.valid asc")
	Collection<Concept> findMyConcept(int id);
	
	@Query("select c from Concept c where c.day is not null and day(current_date)=day(c.day) and month(current_date)=month(c.day) and year(current_date)=year(c.day) and hour(current_date)+1<c.ttl")
	Collection<Concept> getAuction();
	
	@Query("select c from Concept c where c.valid=0")
	Collection<Concept> findNotValid();
	
	@Query("select count(c) from Concept c where c.valid=1 and c.day is not null and day(current_date)=day(c.day) and month(current_date)=month(c.day) and year(current_date)=year(c.day) and c.crown.id=?1")
	Integer getActiveConcept(int id);
}