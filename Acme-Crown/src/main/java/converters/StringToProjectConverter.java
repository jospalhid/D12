package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ProjectRepository;
import domain.Project;

@Component
@Transactional
public class StringToProjectConverter implements Converter<String, Project>{

	@Autowired
	ProjectRepository projectRepository;

	@Override
	public Project convert(String text) {
		Project result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = projectRepository.findOne(id);
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
