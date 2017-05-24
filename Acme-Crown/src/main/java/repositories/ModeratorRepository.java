package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Moderator;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {

	@Query("select a from Moderator a where a.userAccount.id = ?1")
	Moderator findByUserAccountId(int id);
	
	@Query("select m from Moderator m where m.banned=false")
	Collection<Moderator> findAllNotBanned();
}