package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.BidderRepository;
import repositories.CrownRepository;
import domain.SuperUser;

@Component
@Transactional
public class StringToSuperUserConverter implements Converter<String, SuperUser>{
	
	@Autowired
	CrownRepository crownRepository;
	@Autowired
	BidderRepository bidderRepository;

	@Override
	public SuperUser convert(String text) {
		SuperUser result;
		int id;

		try {
			id = Integer.valueOf(text);
			
			result = bidderRepository.findOne(id);
			
			if(result == null){
				result = crownRepository.findOne(id);
			}
			
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
