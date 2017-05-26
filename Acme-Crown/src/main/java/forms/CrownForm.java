
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;

import domain.CreditCard;

@Access(AccessType.PROPERTY)
public class CrownForm {

	//----------------------Attributes-------------------------

	//Useraccount attributes
	private String			username;
	private String			password1;
	private String			password2;

	//Crown attributes
	private String			name;
	private String			surname;
	private String			email;
	private String			phone;
	private String			picture;

	private CreditCard		creditCard;

	private final String[]	conditions	= {};


	public CrownForm() {
		super();
	}

	//----------------------Getters&Setters-------------------------
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword1() {
		return this.password1;
	}

	public void setPassword1(final String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return this.password2;
	}

	public void setPassword2(final String password2) {
		this.password2 = password2;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public String[] getConditions() {
		return this.conditions;
	}

}
