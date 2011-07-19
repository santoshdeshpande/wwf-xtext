package com.jda.webworks.config.validation;

import org.eclipse.xtext.validation.Check;

import com.jda.webworks.config.config.ConfigPackage;
import com.jda.webworks.config.config.ConfigProperty;
 

public class ConfigJavaValidator extends AbstractConfigJavaValidator {

//	@Check
//	public void checkGreetingStartsWithCapital(Greeting greeting) {
//		if (!Character.isUpperCase(greeting.getName().charAt(0))) {
//			warning("Name should start with a capital", MyDslPackage.Literals.GREETING__NAME);
//		}
//	}
	
	@Check
	public void checkValueIsNotEmpty(ConfigProperty property) {
		String value = property.getValue();
		if(value == null || value.trim().equals(""))
			error("The Value for the config property cannot be empty",ConfigPackage.Literals.CONFIG_PROPERTY__VALUE);
	}

}
