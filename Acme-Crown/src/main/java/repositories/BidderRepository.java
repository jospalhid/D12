package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Admin;
import domain.Bidder;

@Repository
public interface BidderRepository extends JpaRepository<Bidder, Integer> {

	@Query("select a from Bidder a where a.userAccount.id = ?1")
	Admin findByUserAccountId(int id);
}