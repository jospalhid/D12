
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Bidder extends SuperUser {

	//---------------------Relationships--------------------------
	public Collection<Bid> bids;

	@Valid
	@NotNull
	@OneToMany(mappedBy = "bidder")
	public Collection<Bid> getBids() {
		return bids;
	}

	public void setBids(Collection<Bid> bids) {
		this.bids = bids;
	}
	
}
