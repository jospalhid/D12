package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Crown;

@Repository
public interface CrownRepository extends JpaRepository<Crown, Integer> {

	@Query("select a from Crown a where a.userAccount.id = ?1")
	Crown findByUserAccountId(int id);
}