package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.CrownRepository;
import domain.Crown;

@Component
@Transactional
public class StringToCrownConverter implements Converter<String, Crown>{

	@Autowired
	CrownRepository crownRepository;

	@Override
	public Crown convert(String text) {
		Crown result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = crownRepository.findOne(id);
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
