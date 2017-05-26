package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

	@Query("select b.input from Bid b where b.concept.id=?1")
	Collection<Double> getAllBids(int id);
}