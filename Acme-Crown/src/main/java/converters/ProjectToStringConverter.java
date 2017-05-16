package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Project;


@Component
@Transactional
public class ProjectToStringConverter implements Converter<Project, String>{

	@Override
	public String convert(Project project) {
		String result;

		if (project == null)
			result = null;
		else
			result = String.valueOf(project.getId());

		return result;
	}
}
