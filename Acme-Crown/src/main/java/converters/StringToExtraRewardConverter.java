package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ExtraRewardRepository;
import domain.ExtraReward;

@Component
@Transactional
public class StringToExtraRewardConverter implements Converter<String, ExtraReward>{

	@Autowired
	ExtraRewardRepository extraRewardRepository;

	@Override
	public ExtraReward convert(String text) {
		ExtraReward result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = extraRewardRepository.findOne(id);
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
