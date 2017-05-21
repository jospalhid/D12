package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
public class Moderator extends Actor{

	//----------------------Attributes-------------------------
	private int range;

	@Range(min=1, max=2)
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	
	//---------------------Relationships--------------------------
	public Crown crown;

	@Valid
	@NotNull
	@OneToOne(optional=false)
	public Crown getCrown() {
		return crown;
	}
	public void setCrown(Crown crown) {
		this.crown = crown;
	}
	
}
