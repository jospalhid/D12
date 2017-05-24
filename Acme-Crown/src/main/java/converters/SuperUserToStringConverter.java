package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.SuperUser;


@Component
@Transactional
public class SuperUserToStringConverter implements Converter<SuperUser, String>{
	
	@Override
	public String convert(SuperUser superUser) {
		String result;

		if (superUser == null)
			result = null;
		else
			result = String.valueOf(superUser.getId());

		return result;
	}

}
