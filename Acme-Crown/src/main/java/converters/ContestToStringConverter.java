package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Contest;


@Component
@Transactional
public class ContestToStringConverter implements Converter<Contest, String>{

	@Override
	public String convert(Contest contest) {
		String result;

		if (contest == null)
			result = null;
		else
			result = String.valueOf(contest.getId());

		return result;
	}
}
