package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
	
	@Query("select sum(r.cost) from Reward r join r.crowns where r.project.id=?1")
	Double getCurrentGoal(int projectId);
	
	@Query("select count(r) from Reward r join r.crowns where r.project.id=?1")
	Integer getBackers(int projectId);
	
	@Query("select p from Project p where p.banned=false and month(current_date)<=month(p.ttl) and year(current_date)=year(p.ttl)")
	Collection<Project> findAvailableProjects();
	
	@Query("select p from Project p where p.crown.userAccount.id=?1")
	Collection<Project> findMyProjects(int id);
	
	@Query("select distinct r.project from Reward r join r.crowns c where c.userAccount.id=?1 and r.project.banned=false")
	Collection<Project> findMyContributions(int id);
	
	@Query("select p from Project p where (year(current_date)<year(p.ttl) or (year(current_date)=year(p.ttl) and month(current_date)<month(p.ttl)) or (year(current_date)=year(p.ttl) and month(current_date)=month(p.ttl) and day(current_date)<day(p.ttl))) and p.banned=false and p.crown.userAccount.id=?1")
	Collection<Project> findMyAvailableProjects(int id);

}