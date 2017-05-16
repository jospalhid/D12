package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;


@Component
@Transactional
public class CreditCardToStringConverter implements Converter<CreditCard, String>{

	@Override
	public String convert(CreditCard card) {
		String result;

		if (card == null)
			result = null;
		else
			result = String.valueOf(card.getId());

		return result;
	}
}
