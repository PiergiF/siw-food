package it.uniroma3.siw.siwfood.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.repository.ChefRepository;


@Component
public class ChefValidator implements Validator{
    @Autowired
	private ChefRepository chefRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Chef chef = (Chef)o;
		if (chef.getName()!=null && chef.getSurname()!=null 
				&& chefRepository.existsByNameAndSurname(chef.getName(), chef.getSurname())) {
			errors.reject("chef.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Chef.class.equals(aClass);
	}
}
