package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Embeddable
@Access(AccessType.PROPERTY)
public class Picture {

//-------------Attributes----------------------
	private String url;

	@NotBlank
	@SafeHtml
	@URL
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	
}
