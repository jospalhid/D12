
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sms;

@Repository
public interface SmsRepository extends JpaRepository<Sms, Integer> {

	@Query("select s from Sms s where s.recipient.userAccount.id=?1 order by s.moment desc")
	Collection<Sms> findMyReceivedMessages(int uaId);

	@Query("select s from Sms s where s.sender.userAccount.id=?1 order by s.moment desc")
	Collection<Sms> findMySendMessages(int uaId);
	
	@Query("select count(s) from Sms s where s.recipient.userAccount.id=?1 and s.readed=false")
	Integer unreadCount(int id);
}
