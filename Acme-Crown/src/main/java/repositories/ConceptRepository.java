package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Concept;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Integer> {

}