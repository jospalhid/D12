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

import services.CrownService;
import services.SmsService;
import utilities.AbstractTest;
import domain.Crown;
import domain.Sms;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SendSmsest extends AbstractTest {

	/*
	 * Send a Sms - Crown
	 *
	 * -El orden de los parámetros es: Usuario (Manager) que se va a autenticar,
	 * asunto, cuerpo del mensaje y Error esperado
	 * 
	 * Cobertura del test:
	 * -El usuario autenticado existe y se puede crear el mensaje(test positivo)
	 * -El usuario no está autenticado y no se puede crear el mensaje (test negativo)
	 */
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private CrownService crownService;

	private List<Crown> crowns;
	
	
	@Before
    public void setup() {
		this.crowns = new ArrayList<Crown>();
		this.crowns.addAll(this.crownService.findAll());
		
		Collections.shuffle(this.crowns);
	}
	@Test
	public void driver() {
		final Object testingData[][] = {
				{this.crowns.get(0).getUserAccount().getUsername(),"subject","bdoy",null },
				{"","subject","body",IllegalArgumentException.class}
				};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],
					(String) testingData[i][1],
					(String) testingData[i][2],
					(Class<?>) testingData[i][3]);
	}

	protected void template(String username,String subject, String body, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			Sms sms=smsService.create(crowns.get(0), crowns.get(0));
			sms.setBody(body);
			sms.setSubject(subject);
			smsService.save(sms);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
