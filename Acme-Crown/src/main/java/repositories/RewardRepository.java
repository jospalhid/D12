package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Reward;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Integer> {

}