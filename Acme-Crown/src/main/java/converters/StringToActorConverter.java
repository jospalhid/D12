package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AdminRepository;
import repositories.BidderRepository;
import repositories.CrownRepository;
import repositories.ModeratorRepository;
import domain.Actor;

@Component
@Transactional
public class StringToActorConverter implements Converter<String, Actor>{
	
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	CrownRepository crownRepository;
	@Autowired
	ModeratorRepository moderatorRepository;
	@Autowired
	BidderRepository bidderRepository;

	@Override
	public Actor convert(String text) {
		Actor result;
		int id;

		try {
			id = Integer.valueOf(text);
			
			result = adminRepository.findOne(id);
			
			if(result == null){
				result = crownRepository.findOne(id);
			}
			if(result==null){
				result = moderatorRepository.findOne(id);
			}
			if(result == null){
				result = bidderRepository.findOne(id);
			}
			
			
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
