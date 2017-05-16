package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Reward;


@Component
@Transactional
public class RewardToStringConverter implements Converter<Reward, String>{

	@Override
	public String convert(Reward reward) {
		String result;

		if (reward == null)
			result = null;
		else
			result = String.valueOf(reward.getId());

		return result;
	}
}
