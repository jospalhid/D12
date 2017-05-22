package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Contest;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
	
	@Query("select c from Contest c where month(current_date)<=month(c.moment) and year(current_date)=year(c.moment)")
	Collection<Contest> findAvailableContest();
	
}