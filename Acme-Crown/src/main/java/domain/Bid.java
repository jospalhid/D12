package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Access(AccessType.PROPERTY)
public class Bid extends DomainEntity{

	//----------------------Attributes-------------------------
	private double input;
	private Date moment;
	
	@Min(0)
	public double getInput() {
		return input;
	}
	public void setInput(double input) {
		this.input = input;
	}
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
	public Date getMoment() {
		return moment;
	}
	public void setMoment(Date moment) {
		this.moment = moment;
	}
	//---------------------Relationships--------------------------
	private Bidder bidder;
	private Concept concept;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Bidder getBidder() {
		return bidder;
	}
	public void setBidder(Bidder bidder) {
		this.bidder = bidder;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Concept getConcept() {
		return concept;
	}
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	
	
}