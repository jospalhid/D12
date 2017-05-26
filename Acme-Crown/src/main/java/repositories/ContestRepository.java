package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Contest;
import domain.Project;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
	
	@Query("select c from Contest c where year(current_date)<year(c.moment) or ( year(current_date)=year(c.moment) and month(current_date)<=month(c.moment))")
	Collection<Contest> findAvailableContest();
	
	@Query("select c from Contest c where c.win=false and year(current_date)>year(c.moment) or ( year(current_date)=year(c.moment) and month(current_date)>month(c.moment)) and c.winner is null")
	Collection<Contest> findNotWinner();
	
	@Query("select c.projects from Contest c join c.projects p where c.id=?1 and (select avg(com.stars) from Comment com where com.project.id=p.id)>=4.5")
	Collection<Project> getWinner(int id);
}