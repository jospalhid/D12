
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public abstract class SuperUser extends Actor {

	//----------------Relationships------------------------------------------
	private CreditCard			creditCard;
	
	@Valid
	@OneToOne(mappedBy = "superUser", optional = true)
	public CreditCard getCreditCard() {
		return this.creditCard;
	}
	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}
}
