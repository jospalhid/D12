
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.SmsRepository;
import domain.Sms;

@Component
@Transactional
public class StringToSmsConverter implements Converter<String, Sms> {

	@Autowired
	SmsRepository	messageRepository;


	@Override
	public Sms convert(final String text) {
		Sms result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.messageRepository.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
