package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.CreditCardRepository;
import domain.CreditCard;

@Component
@Transactional
public class StringToCreditCardConverter implements Converter<String, CreditCard>{

	@Autowired
	CreditCardRepository creditCardRepository;

	@Override
	public CreditCard convert(String text) {
		CreditCard result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = creditCardRepository.findOne(id);
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
