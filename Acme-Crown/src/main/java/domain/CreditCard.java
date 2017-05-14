package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Embeddable
@Access(AccessType.PROPERTY)
public class CreditCard {

//-------------Attributes----------------------
	private String holder;
	private String brand;
	private String number;
	private int expirationMonth;
	private int expirationYear;
	private int cvv;
	
	
	@NotBlank
	@SafeHtml
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	
	@NotBlank	
	@SafeHtml
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@CreditCardNumber
	@SafeHtml
	@NotBlank
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Range(min = 1, max = 12)
	public int getExpirationMonth() {
		return expirationMonth;
	}
	public void setExpirationMonth(int expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	
	public int getExpirationYear() {
		return expirationYear;
	}
	public void setExpirationYear(int expirationYear) {
		this.expirationYear = expirationYear;
	}
	
	@Range(min = 100, max = 999)
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}

	
	
}
