
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Access(AccessType.PROPERTY)
public class ActorForm {

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

	private String[]	conditions	= {};


	public ActorForm() {
		super();
	}

	//----------------------Getters&Setters-------------------------
	@NotBlank
	@SafeHtml
	public String getUsername() {
		return this.username;
	}
	public void setUsername(final String username) {
		this.username = username;
	}

	@NotBlank
	@SafeHtml
	public String getPassword1() {
		return this.password1;
	}
	public void setPassword1(final String password1) {
		this.password1 = password1;
	}

	@NotBlank
	@SafeHtml
	public String getPassword2() {
		return this.password2;
	}
	public void setPassword2(final String password2) {
		this.password2 = password2;
	}

	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml
	public String getSurname() {
		return this.surname;
	}
	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@NotBlank
	@SafeHtml
	@Email
	public String getEmail() {
		return this.email;
	}
	public void setEmail(final String email) {
		this.email = email;
	}

	@Pattern(regexp = "([+][0-9]{3})[ ]*([(][0-9]{3}[)])?[ ]*([0-9][ -]*){4,}")
	@SafeHtml
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@NotNull
	@URL
	public String getPicture() {
		return this.picture;
	}
	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public String[] getConditions() {
		return this.conditions;
	}
	public void setConditions(String[] conditions){
		this.conditions = conditions;
	}
	
}
