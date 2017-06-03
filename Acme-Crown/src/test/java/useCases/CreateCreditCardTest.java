package useCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import services.CreditCardService;
import services.CrownService;
import utilities.AbstractTest;
import domain.CreditCard;
import domain.Crown;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CreateCreditCardTest extends AbstractTest {

	/*
	 * Create an event - Manager
	 *
	 * -El orden de los parámetros es: Usuario (Manager) que se va a autenticar,holder,
	 * brand, number, expiration month, expiration year, cvv y Error esperado
	 * 
	 * Cobertura del test:
	 * -El usuario autenticado existe y se puede crear la neuva credit card(test positivo)
	 * -El usuario no está autenticado y no se puede crear la nueva credit card(test negativo)
	 * */
	@Autowired
	private CrownService crownService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	
	private List<Crown> crowns;

	
	@Before
    public void setup() {
		this.crowns= new ArrayList<Crown>();
		this.crowns.addAll(this.crownService.findAll());
		
		Collections.shuffle(this.crowns);
		
	}
	@Test
	public void driver() {
		final Object testingData[][] = {
				{this.crowns.get(0).getUserAccount().getUsername(),"Trafalgar","MASTERCARD","6011238943402529",12,17,408, null },
				{null,"Trafalgar","MASTERCARD","6011238943402529",12,17,408, IllegalArgumentException.class},
				};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],
					(String) testingData[i][1],
					(String) testingData[i][2],
					(String) testingData[i][3],
					(int) testingData[i][4],
					(int) testingData[i][5],
					(int) testingData[i][6],
					(Class<?>) testingData[i][7]);
	}

	protected void template(String username,String holder, String brand, String number,
			int month, int year,int cvv,final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			
			Crown c= crownService.findByUserAccountId(LoginService.getPrincipal().getId());
			
			CreditCard cc= creditCardService.create(c);
			cc.setHolder(holder);
			cc.setBrand(brand);
			cc.setExpirationMonth(month);
			cc.setExpirationYear(year);
			cc.setCvv(cvv);

			creditCardService.save(cc);
			
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
