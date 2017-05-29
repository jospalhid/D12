
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "readed")})
public class Sms extends DomainEntity {

	//----------------------Attributes-------------------------
	private Date	moment;
	private String	subject;
	private String	body;
	private boolean	readed;


	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	public String getSubject() {
		return this.subject;
	}
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotNull
	public String getBody() {
		return this.body;
	}
	public void setBody(final String body) {
		this.body = body;
	}

	public boolean isReaded() {
		return this.readed;
	}

	public void setReaded(final boolean readed) {
		this.readed = readed;
	}


	//---------------------Relationships--------------------------
	private Actor	sender;
	private Actor	recipient;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getSender() {
		return this.sender;
	}
	public void setSender(final Actor sender) {
		this.sender = sender;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getRecipient() {
		return this.recipient;
	}
	public void setRecipient(final Actor recipient) {
		this.recipient = recipient;
	}
}
