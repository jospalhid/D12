package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Concept;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Integer> {
	
	@Query("select c from Concept c where c.crown.userAccount.id=?1")
	Collection<Concept> findMyConcept(int id);

}