package useCases;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.Authority;
import security.UserAccount;
import services.BidderService;
import utilities.AbstractTest;
import domain.Bidder;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterBidderTest extends AbstractTest {

	/*
	 * *----Registrar un Bidder. -----* 
	 * -El orden de los parámetros es: nombre de usuario, contraseña, nombre, apellidos, email,
	 *teléfono, foto y error esperado
	 * 
	 * 
	 * Cobertura del test: 
	 * 		//Todos los atributos correctos por lo tanto se puede registrar y autenticar (test positivo) 
	 * 		//El patrón del atributo teléfono no es correcto (test negativo)
	 */

	@Autowired
	private BidderService bidderService;

	@Test
	public void driver() {
		final Object testingData[][] = {
				{ "bidderTest", "password", "Name", "Surname", "email@gmail.com",
						"+34122332687", "http://www.guapo.es",
						 null },
				{ "bidderTest2", "password", "Name", "Surname", "email@gmail.com",
						"412", "http://www.guapo.es",
						 ConstraintViolationException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],
					(String) testingData[i][1], 
					(String) testingData[i][2],
					(String) testingData[i][3], 
					(String) testingData[i][4],
					(String) testingData[i][5], 
					(String) testingData[i][6],
					(Class<?>) testingData[i][7]);
	}

	protected void template(String username, String password, String name,
			String surname, String email, String phone, String picture,
			Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {

			final Collection<Authority> authorities = new ArrayList<Authority>();
			final Authority a = new Authority();
			a.setAuthority(Authority.BIDDER);
			authorities.add(a);

			final UserAccount ua = new UserAccount();
			ua.setUsername(username);
			ua.setPassword(password);
			ua.setAuthorities(authorities);

			final Bidder b = this.bidderService.create(ua);
			b.setName(name);
			b.setSurname(surname);
			b.setEmail(email);
			b.setPhone(phone);

			b.setPicture(picture);
		

			Bidder save = this.bidderService.save(b);
			authenticate(save.getUserAccount().getUsername());
			unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
