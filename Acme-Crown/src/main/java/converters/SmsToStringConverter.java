
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Sms;

@Component
@Transactional
public class SmsToStringConverter implements Converter<Sms, String> {

	@Override
	public String convert(final Sms sms) {
		String result;

		if (sms == null)
			result = null;
		else
			result = String.valueOf(sms.getId());

		return result;
	}
}
