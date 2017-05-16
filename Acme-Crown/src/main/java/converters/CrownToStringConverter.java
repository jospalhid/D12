package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Crown;


@Component
@Transactional
public class CrownToStringConverter implements Converter<Crown, String>{

	@Override
	public String convert(Crown crown) {
		String result;

		if (crown == null)
			result = null;
		else
			result = String.valueOf(crown.getId());

		return result;
	}
}
