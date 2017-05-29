package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "day, valid")})
public class Concept extends DomainEntity{

	//----------------------Attributes-------------------------
	private String title;
	private String descripcion;
	private int ttl;
	public double dibs;
	public int valid;
	public Date day;
	
	@NotBlank
	@SafeHtml
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotBlank
	@SafeHtml
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Range(min=1, max=23)
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	
	@Min(0)
	public double getDibs() {
		return dibs;
	}
	public void setDibs(double dibs) {
		this.dibs = dibs;
	}
	
	@Range(min=0, max=2)
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	
	//---------------------Relationships--------------------------
	private Crown crown;
	private Collection<Bid> bids;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Crown getCrown() {
		return crown;
	}
	public void setCrown(Crown crown) {
		this.crown = crown;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "concept")
	public Collection<Bid> getBids() {
		return bids;
	}
	public void setBids(Collection<Bid> bids) {
		this.bids = bids;
	}
	
	
	
}
