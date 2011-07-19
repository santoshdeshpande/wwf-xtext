package com.jda.webworks.config.ui.outline;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

import com.google.inject.Inject;
import com.jda.webworks.config.config.Categories;
import com.jda.webworks.config.config.Configuration;

public class ConfigOutlineLabelProvider extends DefaultEObjectLabelProvider {

	@Inject    
    public ConfigOutlineLabelProvider(AdapterFactoryLabelProvider delegate) {
        super(delegate);
    }
	
	public String text(Categories categories) {
		return "Category : " + categories.getName();
	}
	
	public String text(Configuration configuration) {
		return "Webworks Configuration";
	}
}
