package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.RewardRepository;
import domain.Reward;

@Component
@Transactional
public class StringToRewardConverter implements Converter<String, Reward>{

	@Autowired
	RewardRepository rewardRepository;

	@Override
	public Reward convert(String text) {
		Reward result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = rewardRepository.findOne(id);
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
