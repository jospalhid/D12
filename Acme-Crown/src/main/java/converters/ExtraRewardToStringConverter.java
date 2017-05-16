package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.ExtraReward;


@Component
@Transactional
public class ExtraRewardToStringConverter implements Converter<ExtraReward, String>{

	@Override
	public String convert(ExtraReward extraReward) {
		String result;

		if (extraReward == null)
			result = null;
		else
			result = String.valueOf(extraReward.getId());

		return result;
	}
}
