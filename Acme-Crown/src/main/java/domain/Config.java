package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Min;


@Entity
@Access(AccessType.PROPERTY)
public class Config extends DomainEntity{

	//----------------------Attributes-------------------------
	private double auction;
	private double fee;
	
	@Min(0)
	public double getAuction() {
		return auction;
	}
	public void setAuction(double auction) {
		this.auction = auction;
	}
	
	@Min(0)
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	
	
	
}
