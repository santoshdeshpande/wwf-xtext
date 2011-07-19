/*
 * generated by Xtext
 */
package com.jda.webworks.config.ui.outline;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider;
import org.eclipse.xtext.ui.editor.utils.TextStyle;
import org.eclipse.xtext.ui.label.StylerFactory;

import com.google.inject.Inject;
import com.jda.webworks.config.config.BOOL;
import com.jda.webworks.config.config.Categories;
import com.jda.webworks.config.config.ConfigProperty;
import com.jda.webworks.config.config.Configuration;
import com.jda.webworks.config.config.SCOPE_TYPES;

/**
 * customization of the default outline structure
 * 
 */
public class ConfigOutlineTreeProvider extends DefaultOutlineTreeProvider {

	@Inject 
	private StylerFactory stylerFactory;

	public Object _text(Configuration entity) {
		return "Webworks Config";
	}
	
	public Object _text(ConfigProperty configProperty) {
		String result = configProperty.getName();
		StyledString name = new StyledString(result);
		StyledString styledString = new StyledString(" [" + configProperty.getValue() + "]", 
	            stylerFactory
	                .createXtextStyleAdapterStyler(getTypeTextStyle()));		 
		 return name.append(styledString);

	}
	
	public Image _image(Configuration entity) {
		return new Image(Display.getDefault(), ConfigOutlineTreeProvider.class.getResourceAsStream("/icons/package_obj.gif"));
	}
	
	public Image _image(Categories categories) {
		return new Image(Display.getDefault(), ConfigOutlineTreeProvider.class.getResourceAsStream("/icons/prop_ps.gif"));
	}
	
	public Image _image(ConfigProperty configProperty) {
		String image = configProperty.getScope() == SCOPE_TYPES.LOCAL ? "private_co.gif" : "public_co.gif"; 
		return new Image(Display.getDefault(), ConfigOutlineTreeProvider.class.getResourceAsStream("/icons/"+image));
	}
	
	public Image _decorateImage(ConfigProperty configProperty) {
		if(configProperty.getRestart() == BOOL.TRUE) {
			return new Image(Display.getDefault(), ConfigOutlineTreeProvider.class.getResourceAsStream("/icons/warning_co.gif"));
		}
		return null;
	}

	protected TextStyle getTypeTextStyle() {
		TextStyle textStyle = new TextStyle();
		FontData data = new FontData();
		data.height = 10.0f;
		textStyle.setFontData(data);
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}

}
