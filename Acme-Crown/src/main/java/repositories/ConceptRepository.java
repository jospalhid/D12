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
	
	@Query("select c from Concept c where c.day is not null and day(current_date)=day(c.day) and month(current_date)=month(c.day) and year(current_date)=year(c.day) order by c.ttl desc")
	Collection<Concept> getAuction();
	
	@Query("select c from Concept c where c.valid=0")
	Collection<Concept> findNotValid();
	
	@Query("select count(c) from Concept c where c.valid=1 and c.day is not null and day(current_date)=day(c.day) and month(current_date)=month(c.day) and year(current_date)=year(c.day) and c.crown.id=?1")
	Integer getActiveConcept(int id);
	
	@Query("select distinct c from Concept c join c.bids b where b.input>=c.dibs and c.day is not null and (year(current_date)>year(c.day) or (year(current_date)=year(c.day) and month(current_date)>month(c.day)) or (year(current_date)=year(c.day) and month(current_date)=month(c.day) and day(current_date)>day(c.day))) and b.bidder.userAccount.id=?1 and b.input=(select max(bi.input) from Bid bi where bi.concept.id=c.id)")
	Collection<Concept> findMyWins(int id);
	
}