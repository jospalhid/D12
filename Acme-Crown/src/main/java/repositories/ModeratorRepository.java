package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Crown;
import domain.Moderator;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {

	@Query("select a from Moderator a where a.userAccount.id = ?1")
	Crown findByUserAccountId(int id);
}