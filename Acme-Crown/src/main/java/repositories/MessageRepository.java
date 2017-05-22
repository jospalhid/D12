
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message m where m.recipient.userAccount.id=?1")
	Collection<Message> findMyReceivedMessages(int uaId);

	@Query("select m from Message m where m.sender.userAccount.id=?1")
	Collection<Message> findMySendMessages(int uaId);
}
