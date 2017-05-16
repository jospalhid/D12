package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Embeddable
@Access(AccessType.PROPERTY)
public class Picture {

//-------------Attributes----------------------
	private String url;
	private String alt;
	
	public Picture(){}

	public Picture(String url, String alt) {
		this.url = url;
		this.alt = alt;
	}
	
	@NotBlank
	@SafeHtml
	@URL
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@NotNull
	@SafeHtml
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	
	

	
}
